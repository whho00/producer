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
                        $(aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_REPO)
                        docker build -t $IMAGE_NAME:latest .
                        docker tag $IMAGE_NAME:latest $ECR_REPO/$IMAGE_NAME:latest
                        docker push $ECR_REPO/$IMAGE_NAME:latest
                        """
                    }
                }
            }
        }

        stage('Deploy to EKS') {
            steps {
                withCredentials([file(credentialsId: 'kubeconfig-eks', variable: 'KUBECONFIG')]) {
                    sh """
                    helm upgrade --install $IMAGE_NAME ./chart \
                        --set name=$IMAGE_NAME,image=$ECR_REPO/$IMAGE_NAME:latest
                    """
                }
            }
        }
    }
}

