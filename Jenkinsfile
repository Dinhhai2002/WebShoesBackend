pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        IMAGE_NAME            = 'dinhhai123/webshoes'
        IMAGE_TAG             = "${BUILD_NUMBER}"
    }

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven') {
            steps {
                sh 'mvn --version'
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

        stage('Deploy Production') {
            steps {
                withCredentials([file(credentialsId: 'webshoes-prod-config', variable: 'PROD_CONFIG')]) {
                    sh '''
                        echo "=== Đang deploy WebShoes (không dùng profile) ==="
                        
                        docker stop webshoes || true
                        docker rm webshoes || true
                        docker pull dinhhai123/webshoes:latest

                        docker run -d \
                            --name webshoes \
                            -p 8081:8080 \
                            --restart unless-stopped \
                            -v $PROD_CONFIG:/application.properties:ro \
                            -e SPRING_CONFIG_LOCATION=file:/application.properties \
                            -e JAVA_OPTS="-Xms512m -Xmx1024m" \
                            dinhhai123/webshoes:latest

                        echo "Deploy thành công! Truy cập: http://localhost:8081"
                    '''
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo '''
            ╔══════════════════════════════════════╗
            ║     DEPLOY THÀNH CÔNG 100%           ║
            ║     Truy cập: http://localhost:8081  ║
            ║     Hoặc IP công cộng:8081           ║
            ╚══════════════════════════════════════╝
            '''
        }
        failure {
            echo 'DEPLOY THẤT BẠI - Xem log để kiểm tra lỗi'
        }
    }
}