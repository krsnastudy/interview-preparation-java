This is especially powerful when:
- You're using **Azure DevOps Pipelines** for CI/CD, and
- Your deployment target is **Red Hat OpenShift** (on-prem or OpenShift on Azure - ARO)

---

## ✅ What You’ll Learn:
- How Azure DevOps builds Docker images
- How to push images to ACR or OpenShift registry
- How to deploy to OpenShift using `oc` or Helm

---

## 🧱 Architecture Overview

```plaintext
Azure DevOps CI/CD
     |
     |-- Maven Build → Unit Tests → Docker Build
     |
     |-- Docker Push → ACR/OpenShift Image Registry
     |
     |-- Deploy via oc CLI / Helm → OpenShift Cluster (ARO or on-prem)
```

---

## 🛠 Step-by-Step Setup

### ✅ 1. **Install OpenShift CLI (`oc`) in Azure DevOps agent**
Add this task in your pipeline YAML:

```yaml
- script: |
    curl -LO https://mirror.openshift.com/pub/openshift-v4/clients/oc/latest/linux/oc.tar.gz
    tar -xvf oc.tar.gz
    sudo mv oc /usr/local/bin/
  displayName: 'Install oc CLI'
```

---

### ✅ 2. **Login to OpenShift from Pipeline**

```yaml
- script: |
    oc login https://<openshift-api-url> \
    --token=$(OCP_TOKEN) \
    --insecure-skip-tls-verify=true
  displayName: 'Login to OpenShift'
```

> 🔐 Use `OCP_TOKEN` as a **secret variable** in Azure DevOps for security.

---

### ✅ 3. **Build and Push Docker Image (to OpenShift or ACR)**

```yaml
- task: Docker@2
  displayName: 'Build & Push Image'
  inputs:
    containerRegistry: 'OpenShiftRegistryConnection'
    repository: 'springboot-app'
    command: 'buildAndPush'
    dockerfile: '**/Dockerfile'
    tags: '$(Build.BuildId)'
```

---

### ✅ 4. **Deploy to OCP using oc CLI**

```yaml
- script: |
    oc project myproject
    oc apply -f deployment.yaml
    oc apply -f service.yaml
    oc apply -f route.yaml
  displayName: 'Apply OpenShift Resources'
```

Or use Helm if you're managing apps via Helm Charts:

```yaml
- script: |
    helm upgrade --install springboot-app ./helm-chart \
      --namespace myproject \
      --set image.tag=$(Build.BuildId)
  displayName: 'Helm Deploy to OCP'
```

---

## 🔐 Secrets & ConfigMaps from Azure DevOps

Use the `AzureKeyVault@2` task to fetch secrets securely:

```yaml
- task: AzureKeyVault@2
  inputs:
    connectedServiceName: 'AzureServiceConnection'
    keyVaultName: 'my-keyvault'
    secretsFilter: '*'
```

Then pass them into your `oc` or Helm deployments.

---

## ✅ Interview Talking Points

> “We use Azure DevOps for building and releasing our Java microservices. The artifacts (Docker images) are built and pushed to either ACR or the OpenShift internal registry. Then, using `oc` CLI in the pipeline, we deploy and manage our workloads in OpenShift. Secrets are pulled securely from Azure Key Vault, and we use Helm charts to manage deployment consistency across environments.”

---

## 📌 TL;DR for Interviews

| Feature | Description |
|--------|-------------|
| CI/CD | Azure DevOps Pipelines for build & deployment |
| Container Registry | ACR or OpenShift Image Registry |
| Deployment | `oc` CLI or Helm in pipeline |
| Secrets | Managed using Azure Key Vault |
| Target Platform | OpenShift (ARO or on-prem) |
| Benefits | Centralized DevOps, secure builds, repeatable deployments |

---
For a DevOps pipeline setup using **Core Java**, **Spring Boot**, and **React.js**, here's a YAML pipeline template for **Azure DevOps** or **GitHub Actions**. The pipeline will include stages like **build**, **test**, **deploy**, and **code quality check**. I'll also describe a general DevOps architecture diagram after that.

### Example YAML Pipeline (GitHub Actions)

```yaml
name: CI/CD Pipeline for Spring Boot & React

on:
  push:
    branches:
      - main
  pull_request:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v2

      - name: Set up JDK
        uses: actions/setup-java@v2
        with:
          java-version: '11'

      - name: Build Spring Boot application
        run: |
          cd backend
          ./mvnw clean install -DskipTests

      - name: Build React application
        run: |
          cd frontend
          npm install
          npm run build

  test:
    runs-on: ubuntu-latest
    needs: build
    steps:
      - name: Checkout code
        uses: actions/checkout@v2

      - name: Set up JDK
        uses: actions/setup-java@v2
        with:
          java-version: '11'

      - name: Run Spring Boot tests
        run: |
          cd backend
          ./mvnw test

      - name: Run React tests
        run: |
          cd frontend
          npm test -- --coverage

  deploy:
    runs-on: ubuntu-latest
    needs: test
    steps:
      - name: Checkout code
        uses: actions/checkout@v2

      - name: Deploy to server
        run: |
          echo "Deploying Spring Boot backend and React frontend..."
          # Add deployment script here (e.g., for Kubernetes, Docker, etc.)
          # Example: docker-compose up -d

  quality:
    runs-on: ubuntu-latest
    needs: build
    steps:
      - name: Checkout code
        uses: actions/checkout@v2

      - name: Run SonarQube analysis
        run: |
          cd backend
          ./mvnw sonar:sonar -Dsonar.projectKey=your_project_key
```

#### Key Points:
- **Build**: This step installs dependencies and builds both the backend (`Spring Boot`) and frontend (`React.js`).
- **Test**: Executes unit and integration tests for both the backend and frontend.
- **Deploy**: Can be customized to deploy to any server or platform (e.g., Docker, Kubernetes).
- **Quality**: Integrates with SonarQube for static code analysis.

### DevOps Architecture Diagram (Overview)

Here’s a simple DevOps architecture flow for a typical Spring Boot + React.js application:

1. **Source Control (GitHub/GitLab/Bitbucket)**: The codebase is stored and managed.
2. **CI Pipeline (GitHub Actions/Azure DevOps)**: Automates the build, test, and deploy stages.
    - **Build Stage**: Java (`Spring Boot`) and JavaScript (`React.js`) applications are built.
    - **Test Stage**: Unit tests for both backend and frontend are executed.
    - **Quality Analysis**: Tools like SonarQube or ESLint for code quality and coverage.
3. **Containerization (Docker)**: The backend (Spring Boot) and frontend (React.js) applications are containerized into Docker images.
4. **Orchestration (Kubernetes/Docker Compose)**: The Docker images are orchestrated using Kubernetes or Docker Compose for staging and production environments.
5. **Deployment (Helm, Kubernetes, or Cloud)**: Deployed to the cloud, either AWS, Azure, GCP, or on-premise environments.
6. **Monitoring and Logging (Prometheus, Grafana, ELK Stack)**: Monitor applications in production with performance tracking and logging.

---
