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
                    echo "=== Fichiers pom.xml ==="
                    ls -la pom.xml || echo "Fichier pom.xml non trouvé"
                '''
            }
        }

        stage('4. Analyse SonarQube') {
            steps {
                echo '📊 Analyse SonarQube...'
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        mvn clean verify sonar:sonar \
                          -Dsonar.projectKey=tp-foyer \
                          -Dsonar.projectName="TP Foyer" \
                          -DskipTests=true
                    '''
                }
            }
        }

        /* PARTIE KUBERNETES COMMENTÉE - À réactiver plus tard
        stage('5. Construction de l image Docker') {
            steps {
                echo '🐳 Construction de l image Docker...'
                sh """
                    docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                    docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest
                    echo "Images créées:"
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
                        echo "✓ Images publiées sur Docker Hub"
                    """
                }
            }
        }

        stage('7. Déploiement sur Kubernetes') {
            steps {
                echo '☸️ Déploiement sur Kubernetes...'
                sh """
                    echo "=== Vérification de la connexion Kubernetes ==="
                    kubectl get nodes

                    echo ""
                    echo "=== Déploiement MySQL ==="
                    kubectl apply -f k8s/mysql-pv.yaml
                    kubectl apply -f k8s/mysql-pvc.yaml
                    kubectl apply -f k8s/mysql-deployment.yaml
                    kubectl apply -f k8s/mysql-service.yaml

                    echo ""
                    echo "=== Déploiement Spring Boot ==="
                    kubectl apply -f k8s/spring-deployment.yaml
                    kubectl apply -f k8s/spring-service.yaml

                    echo ""
                    echo "=== Attente du démarrage des pods ==="
                    sleep 10

                    echo ""
                    echo "=== État du déploiement ==="
                    kubectl get pods -n ${NAMESPACE}
                    kubectl get svc -n ${NAMESPACE}
                """
            }
        }
        */
    }

    post {
        success {
            echo '✅ =========================================='
            echo '✅ Pipeline exécuté avec succès!'
            echo "📊 SonarQube: http://192.168.33.10:9000"
            echo '✅ =========================================='
            echo ""
            echo "L'analyse SonarQube est terminée."
            echo "Consultez les résultats sur le serveur SonarQube."
        }
        failure {
            echo '❌ Le pipeline a échoué.'
            echo "Vérifiez les logs pour identifier le problème."
        }
        always {
            sh 'docker system prune -f || true'
        }
    }
}