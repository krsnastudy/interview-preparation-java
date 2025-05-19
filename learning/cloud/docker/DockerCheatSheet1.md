## 🚀 Docker Commands – Interview Cheat Sheet

### ✅ 1. **Basic Docker Commands**

| Task | Command |
|------|---------|
| Check Docker version | `docker --version` |
| View running containers | `docker ps` |
| View all containers (incl. stopped) | `docker ps -a` |
| Start a container | `docker start <container_id>` |
| Stop a container | `docker stop <container_id>` |
| Remove a container | `docker rm <container_id>` |
| Remove an image | `docker rmi <image_id>` |
| Remove all stopped containers | `docker container prune` |
| Show container logs | `docker logs <container_id>` |

---

### ✅ 2. **Image Commands**

| Task | Command |
|------|---------|
| List all images | `docker images` |
| Build image from Dockerfile | `docker build -t <image-name> .` |
| Tag image | `docker tag <image> <repo>:<tag>` |
| Pull image | `docker pull <image-name>` |
| Push image to registry | `docker push <repo>:<tag>` |

---

### ✅ 3. **Run & Manage Containers**

| Task | Command |
|------|---------|
| Run container in background | `docker run -d <image>` |
| Run with port mapping | `docker run -p 8080:8080 <image>` |
| Run with env variables | `docker run -e ENV=prod <image>` |
| Run and mount volume | `docker run -v /host:/container <image>` |
| Attach to running container | `docker attach <container_id>` |
| Exec command in container | `docker exec -it <container_id> bash` |

---

### ✅ 4. **Dockerfile Related**

| Task | Command |
|------|---------|
| Build image | `docker build -t myapp .` |
| Dockerfile commands | `FROM`, `RUN`, `CMD`, `COPY`, `ENTRYPOINT`, `EXPOSE`, `ENV`, etc. |

🧠 **Interview Tip**: Be ready to explain each instruction in the `Dockerfile`.

---

### ✅ 5. **Networking & Volumes**

| Task | Command |
|------|---------|
| List networks | `docker network ls` |
| Create network | `docker network create <name>` |
| Connect container to network | `docker network connect <net> <container>` |
| List volumes | `docker volume ls` |
| Create volume | `docker volume create <volume_name>` |
| Inspect volume | `docker volume inspect <volume_name>` |

---

### ✅ 6. **Docker Compose (Advanced Projects)**

| Task | Command |
|------|---------|
| Start all services | `docker-compose up -d` |
| Stop services | `docker-compose down` |
| View logs | `docker-compose logs` |
| Rebuild images | `docker-compose up --build` |

🧠 Use case: Managing **multi-container apps** like Spring Boot + PostgreSQL + Redis.

---

## 🧾 TL;DR for Interviews:

- **Basic**: `docker ps`, `run`, `build`, `logs`, `rm`
- **Images**: `build`, `tag`, `push`, `pull`
- **Volumes**: `-v`, `docker volume ls`
- **Compose**: `up`, `down`, `logs`, `--build`
- **Debug**: `docker exec -it <cid> bash`

---

Let me know if you'd like:
- A sample `Dockerfile` for Spring Boot
- A mini `docker-compose.yml` example for a microservices setup
- Or Docker + Kubernetes combo interview questions!