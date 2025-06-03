# **Basic Unix/Linux Commands Cheat Sheet**

Here’s a categorized list of **essential Unix/Linux commands** with their purposes:

---

## **1. File & Directory Operations**
| Command | Purpose | Example |
|---------|---------|---------|
| `ls` | List directory contents | `ls -l` (detailed list) |
| `cd` | Change directory | `cd /home/user` |
| `pwd` | Print working directory | `pwd` |
| `mkdir` | Create a directory | `mkdir new_folder` |
| `rmdir` | Remove empty directory | `rmdir empty_folder` |
| `rm` | Remove files/dirs | `rm file.txt` (file) <br> `rm -r folder` (recursive) |
| `cp` | Copy files/dirs | `cp file.txt backup/` |
| `mv` | Move/rename files | `mv old.txt new.txt` |
| `touch` | Create empty file | `touch newfile.txt` |
| `cat` | Display file content | `cat file.txt` |
| `less` / `more` | View file page-by-page | `less large_file.log` |
| `head` | Show first N lines | `head -n 5 file.txt` |
| `tail` | Show last N lines | `tail -n 10 file.log` |
| `find` | Search files/dirs | `find /home -name "*.txt"` |

---

## **2. File Permissions**
| Command | Purpose | Example |
|---------|---------|---------|
| `chmod` | Change permissions | `chmod 755 script.sh` |
| `chown` | Change owner | `chown user:group file.txt` |
| `umask` | Set default permissions | `umask 022` |

---

## **3. System Information**
| Command | Purpose | Example |
|---------|---------|---------|
| `uname` | Show system info | `uname -a` (all details) |
| `df` | Disk space usage | `df -h` (human-readable) |
| `du` | Directory space usage | `du -sh /home` |
| `free` | Memory usage | `free -m` (in MB) |
| `top` / `htop` | Process monitoring | `top` (live system stats) |
| `ps` | List processes | `ps aux` (all processes) |
| `kill` | Terminate process | `kill -9 1234` (force kill PID 1234) |

---

## **4. Networking**
| Command | Purpose | Example |
|---------|---------|---------|
| `ping` | Check connectivity | `ping google.com` |
| `ifconfig` / `ip` | Network interfaces | `ifconfig` (older) <br> `ip a` (newer) |
| `netstat` | Network stats | `netstat -tuln` (open ports) |
| `ssh` | Remote login | `ssh user@192.168.1.1` |
| `scp` | Secure file copy | `scp file.txt user@host:/path` |
| `wget` / `curl` | Download files | `wget https://example.com/file.zip` |

---

## **5. Text Processing**
| Command | Purpose | Example |
|---------|---------|---------|
| `grep` | Search text | `grep "error" log.txt` |
| `sed` | Stream editor | `sed 's/old/new/g' file.txt` |
| `awk` | Text processing | `awk '{print $1}' data.txt` |
| `sort` | Sort lines | `sort file.txt` |
| `uniq` | Filter duplicates | `sort file.txt | uniq` |
| `wc` | Word/line count | `wc -l file.txt` (line count) |

---

## **6. Compression & Archiving**
| Command | Purpose | Example |
|---------|---------|---------|
| `tar` | Archive files | `tar -cvf archive.tar folder/` |
| `gzip` / `gunzip` | Compress/decompress | `gzip file.txt` → `file.txt.gz` |
| `zip` / `unzip` | Zip files | `zip archive.zip file1 file2` |

---

## **7. User Management**
| Command | Purpose | Example |
|---------|---------|---------|
| `whoami` | Current user | `whoami` |
| `passwd` | Change password | `passwd` |
| `useradd` | Add user | `useradd newuser` |
| `usermod` | Modify user | `usermod -aG sudo user` |
| `su` / `sudo` | Switch user / root | `sudo apt update` |

---

## **8. Package Management (Debian/Ubuntu)**
| Command | Purpose | Example |
|---------|---------|---------|
| `apt-get` | Install packages | `sudo apt-get install nginx` |
| `apt-cache` | Search packages | `apt-cache search python` |
| `dpkg` | Debian package manager | `dpkg -i package.deb` |

---

## **9. Miscellaneous**
| Command | Purpose | Example |
|---------|---------|---------|
| `history` | Command history | `history | grep "ssh"` |
| `alias` | Create shortcuts | `alias ll='ls -al'` |
| `cron` | Schedule tasks | `crontab -e` (edit cron jobs) |
| `shutdown` | Power off/restart | `shutdown -h now` (halt) |

---

## **Key Takeaways**
✅ **File Operations:** `ls`, `cd`, `cp`, `mv`, `rm`  
✅ **Permissions:** `chmod`, `chown`  
✅ **System Monitoring:** `top`, `df`, `free`  
✅ **Networking:** `ping`, `ssh`, `scp`  
✅ **Text Processing:** `grep`, `sed`, `awk`
