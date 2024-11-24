pipeline {
    agent {
        kubernetes {
            label 'java-gradle-agent'
            defaultContainer 'jnlp'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: java
    image: eclipse-temurin:17-jdk
    command:
    - cat
    tty: true
    resources:
      requests:
        memory: "2Gi"
        cpu: "2"
      limits:
        memory: "2Gi"
        cpu: "2"
"""
        }
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                container('gradle') {
                    sh './gradlew clean build'
                }
            }
        }
        stage('Test') {
            steps {
                container('gradle') {
                    sh './gradlew test'
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}