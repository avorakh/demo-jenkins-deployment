pipeline {
    agent {
        kubernetes {
            label 'jenkins-agent'
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
    resources:
      requests:
        memory: "2Gi"
        cpu: "2"
      limits:
        memory: "2Gi"
        cpu: "2"
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
        stage('Build without Tests') {
            steps {
                container('gradle') {
                    script {
                        sh './gradlew clean build -x test'
                    }
                }
            }
        }
        stage('Run Unit Tests') {
            steps {
                container('gradle') {
                    script {
                        sh './gradlew test'
                    }
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