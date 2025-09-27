pipeline {
    agent any

    environment {
        AWS_REGION   = "eu-west-2"
        ECR_REPO     = "083928968739.dkr.ecr.eu-west-2.amazonaws.com/producer"
        IMAGE_NAME   = "producer1"
        KUBE_CONFIG  = credentials('kubeconfig-eks')   // Jenkins secret
        AWS_CREDENTIALS = credentials('aws-creds')     // Jenkins secret
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/whho00/producer.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

	stage('Docker Build & Push') {
           steps {
              withAWS(region: "${AWS_REGION}", credentials: 'aws-creds') {
                 script {
                    sh """
                        aws ecr get-login-password --region ${env.AWS_REGION} \
                        | docker login --username AWS --password-stdin ${env.ECR_REPO}
                        docker build -t ${env.IMAGE_NAME}:latest .
                        docker tag ${env.IMAGE_NAME}:latest ${env.ECR_REPO}/${env.IMAGE_NAME}:latest
                        docker push ${env.ECR_REPO}/${env.IMAGE_NAME}:latest
"""
                 }
              }
           }
        }
    }
}

