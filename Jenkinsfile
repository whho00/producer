pipeline {
    agent any

    environment {
        AWS_REGION       = "eu-west-2"
        EKS_REGION       = "eu-west-1"
	EKS_CLUSTER      = "kafka-microservices"
        ECR_REPO     = "083928968739.dkr.ecr.eu-west-2.amazonaws.com"
        IMAGE_NAME   = "producer"
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

        stage('Configure Kubeconfig') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-creds',
                    accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                    secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                ]]) {
                    sh """
            aws eks update-kubeconfig --region ${env.EKS_REGION} --name ${env.EKS_CLUSTER}
"""
                }
            }
        }

        stage('Verify AWS identity') {
            steps {
                sh 'aws sts get-caller-identity'
            }
        }

        stage('Verify cluster access') {
            steps {
                sh 'kubectl get nodes'
            }
        }

        stage('Deploy to EKS') {
            steps {
                withCredentials([file(credentialsId: 'kubeconfig-eks', variable: 'KUBECONFIG')]) {
                    sh """
          helm upgrade --install $IMAGE_NAME ./charts --namespace default \
                --set image.repository=$ECR_REPO/$IMAGE_NAME \
                --set image.tag=latest 
"""
                }
            }
        }


    }
}

