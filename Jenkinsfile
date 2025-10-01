pipeline {
    agent any

    environment {
        AWS_REGION   = "eu-west-2"
        EKS_REGION   = "eu-west-1"
	EKS_CLUSTER  = "kafka-microservices"
        ECR_REPO     = "083928968739.dkr.ecr.eu-west-2.amazonaws.com"
        IMG_NAME     = "producer1"
        IMG_URL      = "${ECR_REPO}/${IMG_NAME}"
    }


    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/whho00/producer1.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh './mvnw clean package'
            }
        }

	stage('Docker Build & Push') {
           steps {
              withAWS(region: "${AWS_REGION}", credentials: 'aws-creds') {
                 script {
def gitCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
env.UNIQUE_TAG = "${env.BUILD_NUMBER}-${gitCommit}"

                    sh """
           aws ecr get-login-password --region ${env.AWS_REGION} \
                | docker login --username AWS --password-stdin ${env.ECR_REPO}
           docker build -t ${env.IMG_NAME} .
           docker tag      ${env.IMG_NAME} ${env.IMG_URL}:${env.UNIQUE_TAG} 
           docker push     ${env.IMG_URL}:${env.UNIQUE_TAG} 
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
              sh """
	helm upgrade --install ${env.IMG_NAME} ./charts \
        --namespace default \
	--set image.repository=${env.IMG_URL} \
	--set image.tag=${env.UNIQUE_TAG} 
"""
                }
            }
        }


    }
}



