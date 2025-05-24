Here is a **complete and simple Kubernetes YAML setup** for deploying a Spring Boot microservice using:

* **Deployment**: Defines pods
* **Service**: Exposes pods internally
* **Ingress / Route**: (Azure = Ingress) Exposes app externally
* **ConfigMap**: External non-sensitive configuration
* **Secrets**: External sensitive data like passwords

This setup is **Azure DevOps-compatible** and can be integrated into pipelines easily.

---

## ✅ 1. `configmap.yaml` – App Configuration

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  APP_ENV: "production"
  APP_MESSAGE: "Welcome to Kubernetes!"
```

> 📘 **Explanation**:
>
> * ConfigMap stores non-sensitive env values
> * These values are injected into pods via env variables

---

## ✅ 2. `secrets.yaml` – Database Credentials

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: db-secret
type: Opaque
data:
  DB_USERNAME: bXl1c2Vy  # base64 of 'myuser'
  DB_PASSWORD: bXlwYXNzd29yZA==  # base64 of 'mypassword'
```

> 📘 **Explanation**:
>
> * `Opaque`: generic secret type
> * Use `echo -n 'myuser' | base64` to encode values

---

## ✅ 3. `deployment.yaml` – App Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: springboot-app
spec:
  replicas: 2
  selector:
    matchLabels:
      app: springboot-app
  template:
    metadata:
      labels:
        app: springboot-app
    spec:
      containers:
        - name: springboot-container
          image: myregistry.azurecr.io/springboot-app:latest
          ports:
            - containerPort: 8080
          env:
            - name: APP_ENV
              valueFrom:
                configMapKeyRef:
                  name: app-config
                  key: APP_ENV
            - name: APP_MESSAGE
              valueFrom:
                configMapKeyRef:
                  name: app-config
                  key: APP_MESSAGE
            - name: DB_USERNAME
              valueFrom:
                secretKeyRef:
                  name: db-secret
                  key: DB_USERNAME
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: db-secret
                  key: DB_PASSWORD
```

> 📘 **Explanation**:
>
> * Pulls image from Azure Container Registry
> * Injects ConfigMap and Secret as env variables
> * `replicas: 2` runs two pods for availability

---

## ✅ 4. `service.yaml` – Internal Load Balancing

```yaml
apiVersion: v1
kind: Service
metadata:
  name: springboot-service
spec:
  selector:
    app: springboot-app
  ports:
    - port: 80
      targetPort: 8080
  type: ClusterIP
```

> 📘 **Explanation**:
>
> * Exposes your pods internally inside the cluster
> * Route traffic from port 80 → container port 8080

---

## ✅ 5. `ingress.yaml` – Public Exposure (Route in Azure AKS)

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: springboot-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  rules:
    - host: springboot.example.com
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: springboot-service
                port:
                  number: 80
```

> 📘 **Explanation**:
>
> * Exposes your service externally
> * Needs Ingress Controller installed (e.g., nginx)
> * Use custom DNS in Azure or Azure Front Door

---

## 🛠️ Azure DevOps Pipeline Integration (Optional)

To apply these YAMLs in a pipeline step:

```yaml
- task: Kubernetes@1
  displayName: 'Deploy to AKS'
  inputs:
    connectionType: 'Azure Resource Manager'
    azureSubscriptionEndpoint: '<Azure Connection>'
    azureResourceGroup: '<Your RG>'
    kubernetesCluster: '<Your AKS Cluster>'
    namespace: 'default'
    command: 'apply'
    useConfigurationFile: true
    configuration: |
      configmap.yaml
      secrets.yaml
      deployment.yaml
      service.yaml
      ingress.yaml
```

---

## ✅ TL;DR Summary

| Component    | Role                                        |
| ------------ | ------------------------------------------- |
| `ConfigMap`  | Store non-sensitive configuration           |
| `Secret`     | Store sensitive info (e.g., DB credentials) |
| `Deployment` | Manages app pods                            |
| `Service`    | Exposes app internally                      |
| `Ingress`    | Exposes app to the internet (Route)         |
| Azure DevOps | Automate deployment using pipeline tasks    |

---

Would you like a **complete downloadable project folder** or a **working sample with Helm chart** for Azure AKS deployment?
