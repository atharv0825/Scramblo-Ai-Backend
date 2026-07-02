pipeline {
    agent any

    environment {
        IMAGE_NAME = "scramblo-ai"
        CONTAINER_NAME = "scramblo-ai-container"

        DB_URL = credentials('AI_DB_URL')
        DB_USERNAME = credentials('AI_DB_USERNAME')
        DB_PASSWORD = credentials('AI_DB_PASSWORD')

        ELASTICSEARCH_URL = credentials('ELASTICSEARCH_URL')
        KAFKA_BOOTSTRAP_SERVERS = credentials('KAFKA_BOOTSTRAP_SERVERS')

        GEMINI_API_KEY = credentials('AI_GEMINI_API_KEY')
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/atharv0825/Scramblo-Ai-Backend.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Stop Old Container') {
            steps {
                sh '''
                docker stop $CONTAINER_NAME || true
                docker rm $CONTAINER_NAME || true
                '''
            }
        }

        stage('Run Container') {
            steps {
                sh '''
                docker run -d \
                --name $CONTAINER_NAME \
                --network scramble-network \
                -p 8082:8085 \
                -e DB_URL="$DB_URL" \
                -e DB_USERNAME="$DB_USERNAME" \
                -e DB_PASSWORD="$DB_PASSWORD" \
                -e ELASTICSEARCH_URL="$ELASTICSEARCH_URL" \
                -e KAFKA_BOOTSTRAP_SERVERS="$KAFKA_BOOTSTRAP_SERVERS" \
                -e GEMINI_API_KEY="$GEMINI_API_KEY" \
                $IMAGE_NAME
                '''
            }
        }

        stage('Verify') {
            steps {
                sh '''
                sleep 30
                docker ps
                docker logs $CONTAINER_NAME --tail 100
                '''
            }
        }
    }
}