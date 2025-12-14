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

        stage('3. Vérification du projet') {
            steps {
                echo '🔍 Vérification du projet...'
                sh '''
                    echo "=== Structure du projet ==="
                    ls -la
                    echo ""
                    echo "=== Fichiers Kubernetes ==="
                    ls -la k8s/ || echo "Dossier k8s/ non trouvé"
                '''
            }
        }

        /* ❌ TESTS UNITAIRES SKIPPÉS TEMPORAIREMENT
        stage('Tests unitaires') {
            steps {
                echo '🧪 Exécution des tests...'
                sh 'mvn test -s settings.xml'
            }
        }
        */

        stage('4. Analyse SonarQube') {
            steps {
                echo '📊 Analyse SonarQube...'
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        mvn clean verify sonar:sonar \
                          -Dsonar.projectKey=tp-foyer \
                          -Dsonar.projectName="TP Foyer" \
                          -DskipTests=true \
                          -s settings.xml
                    '''
                }
            }
        }

        stage('4.5. Package et Deploy sur Nexus') {
            steps {
                echo '📦 Package et déploiement sur Nexus...'
                sh 'mvn clean deploy -DskipTests=true -s settings.xml'
            }
        }

        stage('5. Construction de l image Docker') {
            steps {
                echo '🐳 Construction de l image Docker...'
                sh """
                    docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                    docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest
                    docker images | grep ${DOCKER_IMAGE}
                """
            }
        }

        stage('6. Publication sur Docker Hub') {
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

        stage('7. Déploiement sur Kubernetes') {
            steps {
                echo '☸️ Déploiement sur Kubernetes...'
                sh """
                    kubectl get nodes

                    kubectl apply -f k8s/mysql-pv.yaml
                    kubectl apply -f k8s/mysql-pvc.yaml
                    kubectl apply -f k8s/mysql-deployment.yaml
                    kubectl apply -f k8s/mysql-service.yaml

                    kubectl apply -f k8s/spring-deployment.yaml
                    kubectl apply -f k8s/spring-service.yaml

                    sleep 10

                    kubectl get pods -n ${NAMESPACE}
                    kubectl get svc -n ${NAMESPACE}
                """
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline exécuté avec succès'
            echo '📊 SonarQube: http://192.168.33.10:9000'
            echo "📦 Image Docker: ${DOCKER_IMAGE}:${DOCKER_TAG}"
            echo "☸️ Namespace Kubernetes: ${NAMESPACE}"
        }
        failure {
            echo '❌ Le pipeline a échoué.'
        }
        always {
            sh 'docker system prune -f || true'
        }
    }
}
