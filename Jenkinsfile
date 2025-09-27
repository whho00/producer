pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/whho00/producer.git'
            }
        }

        stage('Build') {
            steps {
                sh 'echo "Building project..."'
                // For example: sh 'mvn clean package' (Java) or npm install (Node)
            }
        }

        stage('Test') {
            steps {
                sh 'echo "Running tests2 ..."'
                // Example: sh 'mvn test' or npm test
            }
        }

        stage('Deploy') {
            steps {
                sh 'echo "Deploying..."'
                // Put deployment script here
            }
        }
    }
}

