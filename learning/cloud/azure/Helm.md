## 🎯 Use Case:
Deploy a Dockerized Spring Boot app using **Helm** to **OpenShift or AKS**, with support for:
- Configurable image and tag
- Resource limits
- Externalized environment variables
- Exposing app via Route (OpenShift) or Service (AKS)

---

## 📁 Helm Chart Structure

```
springboot-app/
├── Chart.yaml
├── values.yaml
├── templates/
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── route.yaml     # for OpenShift
│   └── ingress.yaml   # for AKS (optional)
```

---

## 📄 Chart.yaml

```yaml
apiVersion: v2
name: springboot-app
description: A Helm chart for deploying Spring Boot microservice
version: 1.0.0
appVersion: "1.0"
```

---

## 🛠 values.yaml

```yaml
replicaCount: 2

image:
  repository: myacr.azurecr.io/springboot-app
  tag: latest
  pullPolicy: IfNotPresent

service:
  type: ClusterIP
  port: 8080

resources:
  limits:
    cpu: 500m
    memory: 512Mi
  requests:
    cpu: 200m
    memory: 256Mi

env:
  SPRING_PROFILES_ACTIVE: dev
```

---

## 📦 templates/deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "springboot-app.fullname" . }}
spec:
  replicas: {{ .Values.replicaCount }}
  selector:
    matchLabels:
      app: {{ include "springboot-app.name" . }}
  template:
    metadata:
      labels:
        app: {{ include "springboot-app.name" . }}
    spec:
      containers:
        - name: app
          image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}"
          imagePullPolicy: {{ .Values.image.pullPolicy }}
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: {{ .Values.env.SPRING_PROFILES_ACTIVE }}
          resources:
            {{- toYaml .Values.resources | nindent 12 }}
```

---

## 🌐 templates/service.yaml

```yaml
apiVersion: v1
kind: Service
metadata:
  name: {{ include "springboot-app.fullname" . }}
spec:
  type: {{ .Values.service.type }}
  selector:
    app: {{ include "springboot-app.name" . }}
  ports:
    - port: 80
      targetPort: 8080
```

---

## 🛣 templates/route.yaml (OpenShift Only)

```yaml
apiVersion: route.openshift.io/v1
kind: Route
metadata:
  name: {{ include "springboot-app.fullname" . }}
spec:
  to:
    kind: Service
    name: {{ include "springboot-app.fullname" . }}
  port:
    targetPort: 8080
  tls:
    termination: edge
```

---

## 🌐 templates/ingress.yaml (AKS - Optional)

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: {{ include "springboot-app.fullname" . }}
spec:
  rules:
    - http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: {{ include "springboot-app.fullname" . }}
                port:
                  number: 80
```

---

## ✅ Deployment Command

```bash
helm install springboot-app ./springboot-app --namespace dev --create-namespace
```

To upgrade:
```bash
helm upgrade springboot-app ./springboot-app
```

---

## 🔐 Bonus Tip: Secrets & ConfigMaps
You can easily mount secrets or configmaps in `deployment.yaml` by adding:
```yaml
envFrom:
  - configMapRef:
      name: my-config
  - secretRef:
      name: my-secret
```

---

## ✅ Interview Tip
> “We use **Helm charts** for standardized deployments to **AKS and OpenShift**. Helm lets us templatize environment-specific variables, enforce consistency across environments, and rollback or upgrade microservices efficiently.”

---

