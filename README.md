# demo-jenkins-deployment

---

## Prerequisites

Before starting, ensure you have the following:

1. **Jenkins Environment**:

    - Jenkins installed and running.
    - Kubernetes plugin installed and configured.
    - AWS Credentials plugin installed.

2. **Dependencies**:

    - AWS ECR repository is created.
    - Kubernetes cluster with access credentials.
    - SonarCloud account with valid credentials.

3. **Access Permissions**:

    - Valid credentials  for AWS IAM user with ECR push/pull and `sts:GetCallerIdentity` permissions.

---

## Pipeline Setup

###  **Configuring Jenkins Pipeline**

#### a. Add Pipeline to Jenkins

1. Navigate to Jenkins Dashboard.
2. Create a new multibranch pipeline project.
3. Link the repository containing the `Jenkinsfile`.

#### b. Configure Credentials in Jenkins

1. **AWS Credentials**:

    - Go to Jenkins Dashboard > Manage Jenkins > Manage Credentials.
    - Add a new credential:
        - **Kind**: AWS Credentials.
        - **Access Key ID** and **Secret Access Key**: Your AWS keys.

2. **SonarQube Credentials**:

    - Add the following as secret text credentials:
        - `SONAR_PROJECT_KEY`
        - `SONAR_ORGANIZATION`
        - `SONAR_HOST_URL`
        - `SONAR_TOKEN`

3. **ECR Repository URL**:

    - Add as a secret text credential: `ECR_REPOSITORY`.

4. **Kubernetes Config**:

    - Add the Kubernetes configuration file (base64-encoded) as a secret text credential: `K8S_CONFIG`.

5. **Email Recipients**:

    - Add email recipients as a secret text credential: `EMAILS`.

---

### **Key Configurations**

#### a. Kubernetes Agent

- The pipeline uses a Kubernetes agent with the following containers:
    - `gradle`: For building and testing the application.
    - `docker`: For building and pushing Docker images.
    - `helm`: For deploying applications to Kubernetes.

#### b. Environment Variables

The pipeline uses the following environment variables:

| Variable             | Description                              |
| -------------------- | ---------------------------------------- |
| `SONAR_PROJECT_KEY`  | SonarQube project key.                   |
| `SONAR_ORGANIZATION` | SonarQube organization key.              |
| `SONAR_HOST_URL`     | SonarQube server URL.                    |
| `SONAR_TOKEN`        | SonarQube authentication token.          |
| `DOCKER_IMAGE`       | Docker image name.                       |
| `ECR_REPOSITORY`     | AWS ECR repository URL.                  |
| `AWS_REGION`         | AWS region for ECR.                      |
| `K8S_CONFIG`         | Kubernetes config file (base64-encoded). |
| `RECIPIENTS`         | Email recipients for notifications.      |

---
