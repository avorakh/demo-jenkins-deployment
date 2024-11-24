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
    securityContext:
      runAsUser: 1000
      runAsGroup: 1000
      fsGroup: 1000
    resources:
      requests:
        memory: "2Gi"
        cpu: "2"
      limits:
        memory: "2Gi"
        cpu: "2"
    volumeMounts:
    - name: docker-sock
      mountPath: /var/run/docker.sock
  - name: docker
    image: docker:latest
    command:
    - dockerd-entrypoint.sh
    args:
    - --host=unix:///var/run/docker.sock
    tty: true
    securityContext:
      privileged: true
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
        DOCKER_IMAGE = 'avorakh/demo-web-app'
        ECR_REPOSITORY = credentials('ECR_REPOSITORY')
        AWS_REGION = 'eu-north-1'
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
                        sh ('./gradlew sonar -Dsonar.projectKey=$SONAR_PROJECT_KEY -Dsonar.organization=$SONAR_ORGANIZATION -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.token=$SONAR_TOKEN -Dsonar.branch.name=$BRANCH_NAME')
                    }
                }
            }
        }
        stage('Docker Build and Push') {
            when {
                expression {
                    return params.DOCKER_PUSH
                }
            }
            steps {
                container('gradle') {
                    script {
                        sh './gradlew :demo-web-app:dockerBuildImage'
                    }
                }
                container('docker') {
                    script {
                        sh "aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_REPOSITORY"
                        sh "docker tag $DOCKER_IMAGE:latest $ECR_REPOSITORY/$DOCKER_IMAGE:latest"
                        sh "docker push $ECR_REPOSITORY/$DOCKER_IMAGE:latest"
                    }
                }
            }
        }
        stage('Deploy to K8s') {
            when {
                expression {
                    return params.DOCKER_PUSH
                }
            }
            steps {
                script {
                    echo 'Deploying to Kubernetes cluster...'
                }
            }
        }
    }
    parameters {
        booleanParam(name: 'DOCKER_PUSH', defaultValue: false, description: 'Manually trigger Docker build and push')
    }
    post {
        always {
            cleanWs()
        }
    }
}