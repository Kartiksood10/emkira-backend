pipeline {
    agent any

            tools {
                jdk 'JDK21'
                maven 'Maven3'
            }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'master',
                url: 'https://github.com/Kartiksood10/emkira-backend.git'
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    docker build -t emkira-backend:${BUILD_NUMBER} .
                '''
            }
        }

    }

    post {
        success {
            echo 'Build and tests passed successfully.'
        }
        failure {
            echo 'Build or tests failed. Check logs.'
        }
    }
}
