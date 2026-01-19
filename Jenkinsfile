pipeline {
    // Jenkins can run this pipeline on any available agent (node)
    agent any

    // Tools configured in Jenkins (Global Tool Configuration)
    tools {
        jdk 'JDK21'        // JDK used for building Spring Boot app
        maven 'Maven3'     // Maven used for packaging the project
    }

    stages {

        // =========================
        // STAGE 1 — BUILD AND TEST
        // =========================
        stage('Build and Test') {
            steps {
                // Compile code, run tests, and generate JAR file in target/
                sh 'mvn clean package'
            }
        }

        // =========================
        // STAGE 2 — DOCKER BUILD
        // =========================
        stage('Docker Build') {
            steps {
                // Build Docker image using Dockerfile
                // Tag image with Jenkins build number for traceability
                sh "docker build -t emkira-backend:${BUILD_NUMBER} ."
            }
        }

        // =========================
        // STAGE 3 — DEPLOY
        // =========================
        stage('Deploy Using Docker Compose') {
            steps {
                // Tag the same image as 'latest' so docker-compose can use it
                // This keeps the existing versioned tag AND supports deployment
                sh "docker tag emkira-backend:${BUILD_NUMBER} emkira-backend:latest"

                // Stop existing containers (backend, postgres, redis)
                // Volumes are NOT removed, so DB data remains safe
                sh "docker-compose down"

                // Start containers again using updated Docker image
                sh "docker-compose up -d"
            }
        }
    }

    post {
        // Executed when all stages succeed
        success {
            echo 'CI/CD pipeline successful: build, tests, Docker image created, and application deployed.'
        }

        // Executed if any stage fails
        failure {
            echo 'Pipeline failed. Deployment may not have occurred. Check console logs.'
        }
    }
}
