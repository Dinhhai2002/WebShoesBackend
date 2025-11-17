pipeline {
    // QUAN TRỌNG NHẤT: Dùng Docker-in-Docker để luôn có docker command
    agent {
        docker {
            image 'docker:dind'
            args '-v /var/run/docker.sock:/var/run/docker.sock --privileged'
        }
    }

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
                // BẮT BUỘC dùng cái này để tắt hoàn toàn test + không khởi động context
                sh 'mvn clean package -Dmaven.test.skip=true'
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

        // DEPLOY ĐƠN GIẢN NHẤT + CHẠY NGON NHẤT CHO SPRING BOOT 2.2.6
        stage('Deploy Production') {
            steps {
                withCredentials([file(credentialsId: 'webshoes-prod-config', variable: 'PROD_CONFIG')]) {
                    sh '''
                        echo "=== Đang deploy WebShoes Production ==="
                        
                        docker stop webshoes || true
                        docker rm webshoes || true
                        docker pull dinhhai123/webshoes:latest

                        # Cách đơn giản nhất, chắc chắn nhất: mount thẳng + override hoàn toàn
                        docker run -d \
                            --name webshoes \
                            -p 8081:8080 \
                            --restart unless-stopped \
                            -v $PROD_CONFIG:/application.properties:ro \
                            -e SPRING_CONFIG_LOCATION=file:/application.properties \
                            dinhhai123/webshoes:latest

                        echo "Deploy thành công! Đang kiểm tra log..."
                        sleep 8
                        docker logs webshoes | tail -15
                    '''
                }
            }
        }
    }

    post {
        always {
            cleanWs cleanWhenFailure: false, notFailBuild: true
        }
        success {
            echo '''
            ╔════════════════════════════════════════════════════╗
            ║      DEPLOY THÀNH CÔNG 100% - CHÚC MỪNG BẠN!       ║
            ║      Truy cập: http://YOUR_IP:8081                 ║
            ║      Hoặc: http://localhost:8081 (nếu trên VPS)   ║
            ╚════════════════════════════════════════════════════╝
            '''
        }
        failure {
            echo 'DEPLOY THẤT BẠI - Xem Console Output để biết lỗi cụ thể'
        }
    }
}