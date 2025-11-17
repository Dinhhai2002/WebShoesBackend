pipeline {
    agent {
        docker {
            image 'maven:3.8.6-openjdk-17'
            args '-v /var/run/docker.sock:/var/run/docker.sock --privileged -v maven-repo:/root/.m2'
        }
    }

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        IMAGE_NAME            = 'dinhhai123/webshoes'
        IMAGE_TAG             = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') { steps { checkout scm } }

        stage('Build Maven') {
            steps {
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
                        docker stop webshoes || true
                        docker rm webshoes || true
                        docker pull dinhhai123/webshoes:latest
                        docker run -d --name webshoes -p 8081:8080 --restart unless-stopped \
                            -v $PROD_CONFIG:/application.properties:ro \
                            -e SPRING_CONFIG_LOCATION=file:/application.properties \
                            dinhhai123/webshoes:latest
                        
                        echo "Deploy thành công! Kiểm tra log..."
                        sleep 8
                        docker logs webshoes --tail 20
                    '''
                }
            }
        }
    }

    post {
        always {
            node('built-in') {           // hoặc để trống node() cũng được
                cleanWs()
            }
        }
        success {
            echo '''
            ╔══════════════════════════════════════════════════════════╗
            ║     DEPLOY THÀNH CÔNG 100% - BẠN ĐÃ LÀM ĐƯỢC RỒI!!!      ║
            ║     Truy cập ngay: http://YOUR_IP_OR_LOCALHOST:8081      ║
            ╚══════════════════════════════════════════════════════════╝
            '''
        }
        failure { echo 'THẤT BẠI - Xem log trên' }
    }
}