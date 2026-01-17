pipeline {
    agent any

            tools {
                jdk 'JDK21'
                maven 'Maven3'
            }

    stages {

        stage('Clean Workspace') {
             steps {
                 deleteDir()
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
