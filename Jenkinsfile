pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')  // ID trong Jenkins Credentials
        IMAGE_NAME            = 'dinhhai123/webshoes'
        IMAGE_TAG             = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        // DÙNG DOCKER PIPELINE PLUGIN – KHÔNG CẦN LỆNH docker TRONG CONTAINER NỮA
        stage('Build & Push Docker Image') {
            steps {
                script {
                    // Build image
                    def dockerImage = docker.build("${IMAGE_NAME}:${IMAGE_TAG}", ".")
                    
                    // Push lên Docker Hub (tự động login + logout)
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        dockerImage.push("${IMAGE_TAG}")
                        dockerImage.push('latest')
                    }
                }
            }
        }

        // Dọn dẹp image local để tiết kiệm dung lượng agent
        stage('Cleanup Local Images') {
            steps {
                sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG} || true"
                sh "docker rmi ${IMAGE_NAME}:latest || true"
            }
        }

        // Deploy trực tiếp trên máy đang chạy Jenkins (hoặc server có Docker)
        stage('Deploy') {
            steps {
                sh '''
                    docker stop webshoes || true
                    docker rm webshoes || true
                    docker pull dinhhai123/webshoes:latest
                    docker run -d \
                        --name webshoes \
                        -p 8081:8080 \
                        --restart unless-stopped \
                        dinhhai123/webshoes:latest
                '''
            }
        }
    }

    post {
        always {
            // Không cần docker logout nữa vì docker.withRegistry tự xử lý
            cleanWs()  // dọn workspace (tùy chọn)
        }
        success {
            echo '=== DEPLOY THÀNH CÔNG - WEB SHOES ĐÃ CHẠY TRÊN PORT 8081 ==='
        }
        failure {
            echo '=== DEPLOY THẤT BẠI ==='
        }
    }
}