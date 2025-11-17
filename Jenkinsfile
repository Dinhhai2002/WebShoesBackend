pipeline {
    agent any

    environment {
        // Thay bằng username Docker Hub của bạn
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        IMAGE_NAME = 'dinhhai123/webshoes'  // ví dụ: abc123/myapp
        IMAGE_TAG = "${BUILD_NUMBER}"                // hoặc ${GIT_COMMIT.substring(0,7)}
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                sh "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest"
            }
        }

        stage('Login to Docker Hub') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
            }
        }

        stage('Push Docker Image') {
            steps {
                sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                sh "docker push ${IMAGE_NAME}:latest"
            }
        }

        stage('Cleanup Local Images') {
            steps {
                sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG}"
                sh "docker rmi ${IMAGE_NAME}:latest"
            }
        }

        // Tùy chọn: Deploy ngay trên máy có Docker (hoặc lên server khác qua SSH)
        stage('Deploy to Server') {
            steps {
                script {
                    // Cách 1: Deploy trực tiếp trên máy đang chạy Jenkins
                    sh """
                    docker stop webshoes || true
                    docker rm webshoes || true
                    docker run -d --name webshoes -p 8081:8081 ${IMAGE_NAME}:latest
                    """
                    
                    // Cách 2: Deploy qua SSH đến server khác (xem phần 8)
                }
            }
        }
    }

    post {
        always {
            sh 'docker logout'
        }
        success {
            echo 'Deploy thành công!'
        }
        failure {
            echo 'Deploy thất bại!'
        }
    }
}