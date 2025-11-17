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

        stage('Deploy Production') {
            steps {
                withCredentials([file(credentialsId: 'webshoes-prod-config', variable: 'PROD_CONFIG')]) {
                    sh '''
                        echo "=== Đang deploy WebShoes - External Config Fix ==="
                        
                        # Dừng & xóa container cũ
                        docker stop webshoes || true
                        docker rm webshoes || true
                        
                        # Pull image mới nhất
                        docker pull dinhhai123/webshoes:latest

                        # Tạo thư mục config chuẩn trên host (Spring Boot tự tìm ở đây)
                        mkdir -p /tmp/config
                        
                        # Copy file từ credential vào thư mục chuẩn (tránh lỗi mount trực tiếp)
                        cp $PROD_CONFIG /tmp/config/application.properties
                        
                        # Mount vào /config/ (vị trí chuẩn của Spring Boot) + dùng location kết hợp
                        docker run -d \
                            --name webshoes \
                            -p 8081:8080 \
                            --restart unless-stopped \
                            -v /tmp/config/application.properties:/config/application.properties:ro \
                            -e SPRING_CONFIG_LOCATION="classpath:/application.properties,file:/config/" \
                            -e JAVA_OPTS="-Xms512m -Xmx1024m" \
                            dinhhai123/webshoes:latest

                        echo "Deploy thành công! Chờ 10s để kiểm tra log..."
                        sleep 10
                        docker logs webshoes | tail -20
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