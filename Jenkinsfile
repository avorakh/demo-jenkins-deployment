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

    environment {
        SONAR_PROJECT_KEY = credentials('SONAR_PROJECT_KEY')
        SONAR_ORGANIZATION = credentials('SONAR_ORGANIZATION')
        SONAR_HOST_URL = credentials('SONAR_HOST_URL')
        SONAR_TOKEN = credentials('SONAR_TOKEN')
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
                        junit '**/build/test-results/**/*.xml'
                    }
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                container('gradle') {
                    script {
                        sh """
                        ./gradlew sonar \
                            -Dsonar.projectKey=$SONAR_PROJECT_KEY \
                            -Dsonar.organization=$SONAR_ORGANIZATION \
                            -Dsonar.host.url=$SONAR_HOST_URL \
                            -Dsonar.token=$SONAR_TOKEN \
                            -Dsonar.branch.name=$BRANCH_NAME
                        """
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