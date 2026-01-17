pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    stages {

        stage('Build and Test') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t emkira-backend:${BUILD_NUMBER} ."
            }
        }
    }

    post {
        success {
            echo 'CI pipeline successful: build, tests, and Docker image created.'
        }
        failure {
            echo 'CI pipeline failed. Check console logs.'
        }
    }
}
