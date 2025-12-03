pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "vidancodex/tp-foyer"
        DOCKER_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('1. Récupération du code') {
            steps {
                echo '📥 Clonage du dépôt Git...'
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
                echo '🔍 Vérification du projet Maven...'
                sh '''
                    echo "=== Contenu du répertoire ==="
                    ls -la
                    echo ""
                    echo "=== Configuration Maven ==="
                    cat pom.xml | grep -A 2 "<artifactId>"
                '''
            }
        }

        stage('4. Construction de l image Docker') {
            steps {
                echo '🐳 Construction de l image Docker...'
                sh """
                    docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                    docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest
                    docker images | grep ${DOCKER_IMAGE}
                """
            }
        }

        stage('5. Publication sur Docker Hub') {
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
    }

    post {
        success {
            echo '✅ Pipeline exécuté avec succès!'
            echo "Image: ${DOCKER_IMAGE}:${DOCKER_TAG}"
        }
        failure {
            echo '❌ Le pipeline a échoué.'
        }
        always {
            sh 'docker system prune -f || true'
        }
    }
}