# 🚀 CloudDevOpsProject

Enterprise End-to-End DevOps Project implementing full CI/CD and GitOps workflow on AWS using Terraform, Ansible (with Vault), Jenkins, Kubernetes (EKS), ArgoCD, Prometheus, and Grafana.

---

## 📌 Architecture Overview

This project represents a production-grade DevOps architecture where the entire lifecycle—from infrastructure provisioning to application deployment and monitoring—is fully automated.

* Infrastructure is provisioned using Terraform (modular design).
* Jenkins server is configured automatically using Ansible.
* Sensitive Jenkins credentials are securely stored using **Ansible Vault**.
* Jenkins pipelines use a **Shared Library** for clean and reusable CI logic.
* Kubernetes manifests are managed using **GitOps**.
* ArgoCD is the single source of deployment truth and syncs changes automatically to EKS.
* Prometheus collects metrics from the application and cluster.
* Grafana visualizes metrics through production-ready dashboards.

---

## 🔄 End-to-End Workflow

1. Terraform provisions AWS infrastructure (VPC, EC2, EKS, etc.).
2. Ansible configures the Jenkins EC2 server using dynamic inventory.
3. Jenkins credentials are injected securely via Ansible Vault.
4. Developer pushes code to GitHub.
5. Jenkins pipeline triggers automatically.
6. Docker image is built.
7. Image is scanned using Trivy.
8. Image is pushed to DockerHub.
9. Kubernetes deployment manifest image tag is updated.
10. Updated manifests are pushed to GitHub.
11. ArgoCD detects the change and syncs it automatically to the EKS cluster.
12. Application is deployed/updated in Kubernetes.
13. Prometheus scrapes application and node metrics.
14. Grafana dashboards visualize real-time system and application performance.

---

## 🛠 Technology Stack

* Cloud Provider: AWS
* Infrastructure as Code: Terraform
* Configuration Management: Ansible (with Vault)
* CI Tool: Jenkins (Shared Library)
* CD Tool: ArgoCD (GitOps)
* Containerization: Docker
* Orchestration: Kubernetes (EKS)
* Image Security Scanning: Trivy
* Monitoring & Observability: Prometheus + Grafana
* Terraform Backend: S3

---

## 🖼 Architecture Diagram

![](./Images/project_arch.drawio.png)

---
## ☁ Infrastructure Provisioning (Terraform)

📁 Directory: `terraform/`

### Key Features

* Modular Terraform structure:

  * Network module (VPC, Subnets, IGW, NACL)
  * EC2 module (Jenkins Server)
  * EKS module
* Remote backend using S3
* CloudWatch monitoring enabled for EC2
* Security Groups and IAM roles configured

Terraform manages the full lifecycle of AWS resources.

---

## ⚙ Configuration Management (Ansible)

📁 Directory: `ansible/`

### Highlights

* Role-based Ansible structure
* Dynamic inventory using AWS EC2 plugin
* **Ansible Vault** used to store Jenkins credentials securely
* Fully automated Jenkins setup

### Installed Components

* Git
* Docker
* Java
* Jenkins
* Trivy

### Main Playbook

Roles executed in order:

* git
* docker
* java
* jenkins
* trivy
* jenkins_ci_setup

---

## 🐳 Containerization (Docker)

📁 Directory: `Docker_App/Dockerfile`

* Python Flask application
* Lightweight base image (`python:3.10-slim`)
* Dependencies installed via `requirements.txt`
* Application exposed on port 5000
* Prometheus metrics endpoint enabled at `/metrics`

---

## ☸ Kubernetes Orchestration

📁 Directory: `k8s/`

Contains:

* Namespace definition
* Deployment manifest
* Service manifest

⚠ Kubernetes manifests are **not applied manually** in normal operation.
They are deployed automatically by ArgoCD using GitOps.

---

## 🔁 Continuous Integration (Jenkins)

📁 Locations:

* `Jenkins/Jenkinsfile`
* `Jenkins/jenkins-shared-library/vars/`

### Pipeline Stages

* Build Docker Image
* Security Scan (Trivy)
* Push Image
* Remove Local Image
* Update Kubernetes Manifest (GitOps)

### Shared Library

Reusable Groovy functions abstract pipeline logic for cleaner Jenkinsfiles.

---

## 🚀 Continuous Deployment (ArgoCD)

📁 Directory: `argocd/`

* ArgoCD Application manifest stored in GitHub
* Auto-sync enabled
* Self-healing and pruning enabled
* Kubernetes namespace auto-created

ArgoCD is the **only component** responsible for applying Kubernetes manifests.

---

## 🔄 GitOps Model

* Jenkins updates Kubernetes manifests (image tag only).
* Changes are pushed to GitHub.
* ArgoCD watches the repository.
* ArgoCD syncs the desired state into EKS automatically.

Benefits:

* Versioned deployments
* Easy rollback via Git
* Full traceability

---

## 📊 Monitoring & Observability

📁 Directory: `monitoring/` (Prometheus & Grafana manifests)

### Overview

The project includes a full monitoring stack deployed inside Kubernetes:

* **Prometheus** collects metrics from:

  * Flask application (`/metrics` endpoint)
  * Kubernetes nodes (via Node Exporter)
* **Grafana** visualizes metrics through dashboards.

### Application Metrics

The Flask application exposes Prometheus metrics using the Python Prometheus client.

Exposed metrics include:

* `http_requests_total` – Total number of HTTP requests
* `http_request_duration_seconds` – Request latency histogram
* Python process metrics (CPU, memory, GC stats)

### Prometheus

Prometheus is configured with scrape targets for:

* Application service
* Node Exporter

Prometheus provides:

* Time-series storage
* Powerful PromQL querying
* Target health monitoring

### Grafana

Grafana connects to Prometheus as a data source and provides dashboards for:

* Requests per endpoint
* Request rate (RPS)
* Request latency
* Node CPU & memory usage

### Monitoring Flow

Flask App → Service → Prometheus Scrape → Grafana Dashboards

This ensures full observability of both application-level and infrastructure-level metrics.

---
## 📁 Repository Structure
```bash
iVolve_OJT_Project/
├── ansible
│   ├── group_vars
│   │   └── all
│   ├── inventory
│   └── roles
│       ├── docker
│       ├── git
│       ├── java
│       ├── jenkins
│       ├── jenkins_ci_setup
│       └── trivy
├── argocd
│   └── argocd-run
├── Docker_App
│   ├── static
│   │   └── logos
│   └── templates
├── Images
├── Jenkins
│   └── jenkins-shared-library
│       └── vars
├── k8s
└── terraform
    └── modules
        ├── ec2
        ├── eks
        └── network
```
---

## ▶ Execution Overview

High-level flow:

* Terraform provisions infrastructure.
* Ansible configures Jenkins.
* Jenkins runs CI pipeline on code push.
* ArgoCD deploys application automatically to EKS.

---


## ▶ How to Run the Project

Follow the steps below to run the full project from infrastructure provisioning to application deployment:

### 1️⃣ Provision Infrastructure (Terraform)

```bash
cd terraform
terraform init
terraform apply
```

This will provision:

* VPC and networking resources
* EC2 instance for Jenkins
* EKS cluster
* Required IAM roles and security groups

---

### 2️⃣ Configure Jenkins Server (Ansible)

After infrastructure is created, configure Jenkins automatically using Ansible with dynamic inventory and Vault:

```bash
cd ansible
ansible-playbook -i inventory/aws_ec2.yaml site.yaml --ask-vault-pass
```

This installs:

* Git
* Docker
* Java
* Jenkins
* Jenkins_ci_setup
* Trivy

Jenkins credentials are securely injected using **Ansible Vault**.

---

### 3️⃣ Access Jenkins

* Open browser
* Navigate to: `http://<EC2-External-IP>:8080`
* Configure the pipeline job using the existing `Jenkinsfile`

Push code to GitHub → Jenkins pipeline runs automatically.

---

### 4️⃣ Connect Local Machine to EKS

After EKS is provisioned, update kubeconfig:

```bash
aws eks update-kubeconfig --name eks-cluster --region us-east-1
```

This connects your local kubectl to the EKS cluster.

---

### 5️⃣ Install and Access ArgoCD

After ArgoCD is installed in the cluster:

Get initial admin password:

```bash
kubectl get secret argocd-initial-admin-secret -n argocd -o jsonpath="{.data.password}" | base64 -d
```

Expose ArgoCD server (if needed):

```bash
kubectl edit svc argocd-server -n argocd
```

---

### 6️⃣ Deploy Application via ArgoCD (GitOps)

Apply ArgoCD Application manifest:

```bash
cd argocd/argocd-run/
kubectl apply -f argocd-app.yml
kubectl get svc -n app-namespace 
```

ArgoCD will:

* Monitor the GitHub repository
* Detect manifest changes
* Automatically sync to EKS
* Deploy/update the application
* Access app by the LoadBalancer service `External-IP`
### 7️⃣ Deploy & Access Monitoring Stack (Prometheus & Grafana)

If monitoring manifests (`grafana.yml`, `node-exporter.yml`, `prometheus-config.yml`) are included inside `argocd-run/`, ArgoCD will deploy them automatically to the cluster.

You can verify monitoring resources:

```bash
kubectl get all -n monitoring
```

#### 🔎 Access Prometheus (EKS LoadBalancer)

Check the service external IP:

```bash
kubectl get svc -n monitoring
```

Look for the Prometheus service with `TYPE=LoadBalancer` and copy the `EXTERNAL-IP`.

Open in browser:

```
http://<PROMETHEUS-EXTERNAL-IP>:9090
```

You can check targets at:

```
Status → Targets
```

Ensure:

* Application target is **UP**
* Node exporter target is **UP**

---

#### Access Grafana (EKS LoadBalancer)

Get Grafana external IP:

```bash
kubectl get svc -n monitoring
```

Open in browser:

```
http://<GRAFANA-EXTERNAL-IP>:3000
```

Default credentials:

```
Username: admin
Password: admin
```

After login:

1. Go to **Data Sources**
2. Add Prometheus
3. URL:

```
http://prometheus.monitoring.svc.cluster.local:9090
```

4. Save & Test

Now you can create dashboards using queries such as:

```
rate(http_requests_total[1m])
```

This enables full observability of the deployed application running on EKS.

---


## Demo Video - iVolve Project

[iVolve Project Demo](https://drive.google.com/file/d/1NNemjR5n22CHM4rblo7V2zxKYWYbQJ97/view?usp=drivesdk)

---
## Author
- Mohamed Ahmed Mohamed Taha  

**DevOps & Cloud Engineers**


