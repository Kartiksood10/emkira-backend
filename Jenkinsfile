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
                // Tag image as latest so docker-compose uses it
                sh "docker tag emkira-backend:${BUILD_NUMBER} emkira-backend:latest"

                // Force remove existing containers if they exist
                sh "docker rm -f emkira-backend emkira-postgres emkira-redis || true"

                // Clean compose-managed resources
                sh "docker compose down --remove-orphans"

                // Start fresh containers
                sh "docker compose up -d"
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
