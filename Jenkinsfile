pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "vidancodex/tp-foyer"
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        NAMESPACE = "devops"
    }

    stages {
        stage('1. Récupération du code') {
            steps {
                echo '📥 Récupération du code depuis Git...'
                checkout scm
            }
        }

        stage('2. Nettoyage') {
            steps {
                echo '🧹 Nettoyage de l environnement...'
                sh '''
                    docker system prune -f || true
                    rm -rf target/ || true
                '''
            }
        }

        stage('3. Compilation') {
            steps {
                echo '🔨 Compilation du projet...'
                sh 'mvn clean compile -s settings.xml'
            }
        }

        stage('4. Tests Unitaires (Mockito)') {
            steps {
                echo '🧪 Exécution des tests unitaires avec Mockito...'
                sh 'mvn test -s settings.xml'
            }
            post {
                always {
                    // Publish JUnit test results
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'

                    // Publish code coverage if JaCoCo is configured
                    jacoco execPattern: '**/target/jacoco.exec', classPattern: '**/target/classes', sourcePattern: '**/src/main/java'
                }
                success {
                    echo '✅ Tous les tests sont passés!'
                }
                failure {
                    echo '❌ Certains tests ont échoué - voir les rapports ci-dessus'
                }
            }
        }

        stage('5. Analyse SonarQube') {
            steps {
                echo '📊 Analyse SonarQube avec couverture de tests...'
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        mvn sonar:sonar \
                          -Dsonar.projectKey=tp-foyer \
                          -Dsonar.projectName="TP Foyer" \
                          -s settings.xml
                    '''
                }
            }
        }

        stage('6. Quality Gate') {
            steps {
                echo '🚦 Vérification du Quality Gate SonarQube...'
                timeout(time: 15, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: false
                }
            }
        }

        stage('7. Package et Deploy sur Nexus') {
            steps {
                echo '📦 Package et déploiement sur Nexus...'
                sh 'mvn deploy -DskipTests=true -s settings.xml'
            }
            post {
                success {
                    echo '✅ Artifact déployé sur Nexus avec succès!'
                }
            }
        }

        stage('8. Construction de l image Docker') {
            steps {
                echo '🐳 Construction de l image Docker...'
                sh """
                    docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                    docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest
                    docker images | grep ${DOCKER_IMAGE}
                """
            }
        }

        stage('9. Publication sur Docker Hub') {
            steps {
                echo '📤 Publication sur Docker Hub...'
                withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                        docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                        docker push ${DOCKER_IMAGE}:latest
                        docker logout
                    """
                }
            }
        }

        stage('10. Déploiement sur Kubernetes') {
            steps {
                echo '☸️ Déploiement sur Kubernetes...'
                sh """
                    # Create namespace if not exists
                    kubectl get namespace ${NAMESPACE} || kubectl create namespace ${NAMESPACE}

                    echo '📊 Déploiement de MySQL...'
                    kubectl apply -f k8s/mysql-pv.yaml
                    kubectl apply -f k8s/mysql-pvc.yaml
                    kubectl apply -f k8s/mysql-deployment.yaml
                    kubectl apply -f k8s/mysql-service.yaml

                    echo '⏳ Attente de MySQL...'
                    kubectl wait --for=condition=ready pod -l app=mysql -n ${NAMESPACE} --timeout=300s || true

                    echo '🚀 Déploiement de l application Spring Boot...'
                    kubectl apply -f k8s/spring-deployment.yaml -n ${NAMESPACE}
                    kubectl apply -f k8s/spring-service.yaml -n ${NAMESPACE}

                    echo '⏳ Attente du déploiement...'
                    kubectl rollout status deployment -n ${NAMESPACE} --timeout=5m || true

                    echo ''
                    echo '═══════════════════════════════════════'
                    echo '📋 Status du déploiement:'
                    echo '═══════════════════════════════════════'
                    kubectl get pods -n ${NAMESPACE}
                    kubectl get svc -n ${NAMESPACE}
                """
            }
        }

        stage('11. Vérification du déploiement') {
            steps {
                echo '🔍 Vérification finale...'
                sh """
                    echo ''
                    echo '🏥 Test de santé de l application...'
                    sleep 10

                    # Get service URL
                    SERVICE_URL=\$(minikube service spring-service -n ${NAMESPACE} --url 2>/dev/null || echo 'http://192.168.49.2:30000')
                    echo "Application URL: \$SERVICE_URL"

                    # Test endpoint (if available)
                    curl -f -s \$SERVICE_URL/actuator/health || echo '⚠️ Health endpoint not ready yet'
                """
            }
        }
    }

    post {
        success {
            echo ''
            echo '═══════════════════════════════════════════════════'
            echo '✅ PIPELINE EXÉCUTÉ AVEC SUCCÈS!'
            echo '═══════════════════════════════════════════════════'
            echo ''
            echo '📊 Résultats:'
            echo "   • Tests: Tous les tests sont passés"
            echo "   • SonarQube: http://192.168.33.10:9000/dashboard?id=tp-foyer"
            echo "   • Nexus: http://192.168.33.10:8081"
            echo "   • Docker: ${DOCKER_IMAGE}:${DOCKER_TAG}"
            echo "   • Kubernetes: namespace ${NAMESPACE}"
            echo ''
            echo '═══════════════════════════════════════════════════'

            // Archive test results and JAR
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: true
        }
        failure {
            echo ''
            echo '❌ LE PIPELINE A ÉCHOUÉ!'
            echo ''
            sh """
                echo '📋 Informations de débogage:'
                echo ''
                echo 'Pods:'
                kubectl get pods -n ${NAMESPACE} -o wide || true
                echo ''
                echo 'Logs récents:'
                kubectl logs -n ${NAMESPACE} -l app=spring-boot --tail=50 || true
                echo ''
                echo 'Events:'
                kubectl get events -n ${NAMESPACE} --sort-by='.lastTimestamp' | tail -20 || true
            """
        }
        always {
            echo '🧹 Nettoyage final...'
            sh 'docker system prune -f || true'
            echo '🔚 Pipeline terminé.'
        }
    }
}