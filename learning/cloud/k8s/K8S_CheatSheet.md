
# 📘 Kubernetes Command Cheat Sheet

## 🔍 Context & Cluster Info
kubectl config view                        # View kubeconfig settings
kubectl config current-context             # Show current context
kubectl cluster-info                       # Show cluster details

## 🧱 Namespaces
kubectl get namespaces                     # List all namespaces
kubectl create namespace <name>            # Create a namespace
kubectl delete namespace <name>            # Delete a namespace
kubectl config set-context --current --namespace=<name>  # Set default namespace

## 📦 Pods
kubectl get pods                           # List pods in current namespace
kubectl get pods -n <namespace>            # List pods in a namespace
kubectl describe pod <pod-name>            # Detailed info about a pod
kubectl delete pod <pod-name>              # Delete a pod
kubectl logs <pod-name>                    # View logs
kubectl exec -it <pod-name> -- /bin/bash   # Access pod shell

## 🚀 Deployments
kubectl get deployments                    # List deployments
kubectl create -f deployment.yaml          # Create from YAML
kubectl apply -f deployment.yaml           # Apply changes
kubectl rollout restart deployment <name>  # Restart deployment
kubectl delete deployment <name>           # Delete deployment

## 🔄 Services
kubectl get services                       # List services
kubectl expose deployment <dep> --port=80 --target-port=8080 --type=ClusterIP  # Create service
kubectl delete service <name>              # Delete service

## 📂 ConfigMaps & Secrets
kubectl create configmap my-config --from-literal=key=value
kubectl get configmaps
kubectl describe configmap my-config

kubectl create secret generic my-secret --from-literal=username=admin
kubectl get secrets
kubectl describe secret my-secret

## 📜 YAML Generation (Dry Run)
kubectl create deployment nginx --image=nginx --dry-run=client -o yaml > nginx-deploy.yaml

## 📡 Port Forwarding & Logs
kubectl port-forward pod/<pod-name> 8080:80
kubectl logs -f <pod-name>                         # Follow logs

## 🧹 Cleanup
kubectl delete all --all                           # Delete everything in current namespace
