pipeline {
    agent {
        kubernetes {
            label 'java-gradle'
            defaultContainer 'jnlp'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: gradle
    image: gradle:8.7-jdk17
    command:
    - cat
    tty: true
  - name: docker
    image: docker:latest
    command:
    - cat
    tty: true
    volumeMounts:
    - name: docker-sock
      mountPath: /var/run/docker.sock
  volumes:
  - name: docker-sock
    hostPath:
      path: /var/run/docker.sock
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