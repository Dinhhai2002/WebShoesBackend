pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        IMAGE_NAME            = 'dinhhai123/webshoes'
        IMAGE_TAG             = "${BUILD_NUMBER}"
    }

    // TỰ ĐỘNG CÀI JDK + MAVEN TRƯỚC KHI CHẠY
    tools {
        jdk 'JDK17'      // tên này sẽ tạo ở bước dưới
        maven 'Maven3'   // tên này sẽ tạo ở bước dưới
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven') {
            steps {
                sh 'mvn --version'  // kiểm tra xem đã có mvn chưa
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                script {
                    def dockerImage = docker.build("${IMAGE_NAME}:${IMAGE_TAG}", ".")
                    
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        dockerImage.push("${IMAGE_TAG}")
                        dockerImage.push('latest')
                    }
                }
            }
        }

        stage('Cleanup Local Images') {
            steps {
                sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG} || true"
                sh "docker rmi ${IMAGE_NAME}:latest || true"
            }
        }

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
            cleanWs()
        }
        success {
            echo '=== DEPLOY THÀNH CÔNG - TRUY CẬP http://localhost:8081 ==='
        }
        failure {
            echo '=== DEPLOY THẤT BẠI ==='
        }
    }
}