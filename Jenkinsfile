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

    }
}

