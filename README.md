# Page Turner Books

A simple bookstore web app: Java 17, Spring Boot 3, Thymeleaf, Maven.

## Pages
| URL            | Description                          |
|----------------|--------------------------------------|
| `/`            | Home page with featured books        |
| `/books`       | Catalogue with search (`?q=`)        |
| `/books/{id}`  | Book detail page                     |
| `/about`       | About page                           |

## Requirements
- JDK 17+
- Maven 3.9+

## Build and run locally
```bash
mvn spring-boot:run        # http://localhost:8088
mvn test
mvn clean package
java -jar target/bookstore-0.0.1-SNAPSHOT.jar
```

## Port configuration
The app listens on **port 8088** on all interfaces (`0.0.0.0`).
Change it without rebuilding:
```bash
SERVER_PORT=9090 java -jar target/bookstore-0.0.1-SNAPSHOT.jar
# or
java -jar target/bookstore-0.0.1-SNAPSHOT.jar --server.port=9090
```

## Deploy on a Linux server
Install JDK 17 on the server (e.g. `sudo apt install openjdk-17-jre-headless` or
`sudo dnf install java-17-openjdk-headless`), then copy the project (or just `target/*.jar`
and `deploy/`) to the server.

### Automated
```bash
mvn clean package
sudo ./deploy/deploy.sh            # installs systemd service + opens port 8088
```

### Manual
```bash
sudo useradd --system --no-create-home --shell /usr/sbin/nologin bookstore
sudo mkdir -p /opt/bookstore
sudo cp target/bookstore-0.0.1-SNAPSHOT.jar /opt/bookstore/bookstore.jar
sudo chown -R bookstore:bookstore /opt/bookstore
sudo cp deploy/bookstore.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable --now bookstore
```

### Open the firewall port (8088)
```bash
# Ubuntu / Debian (ufw)
sudo ufw allow 8088/tcp

# RHEL / CentOS / Rocky / Fedora (firewalld)
sudo firewall-cmd --permanent --add-port=8088/tcp
sudo firewall-cmd --reload

# Plain iptables
sudo iptables -I INPUT -p tcp --dport 8088 -j ACCEPT
```
If the server is in a cloud provider (AWS, Azure, GCP, etc.), also allow inbound TCP 8088 in the
security group / network rules.

### Verify
```bash
systemctl status bookstore
journalctl -u bookstore -f
curl -I http://localhost:8088/
ss -tlnp | grep 8088
```
Then browse to `http://<server-ip>:8088`.

## Structure
```
src/main/java/com/example/bookstore
  BookstoreApplication.java      entry point
  model/Book.java                book record
  service/BookService.java       in-memory catalogue
  controller/PageController.java page routes
src/main/resources
  application.properties         port 8088, bind 0.0.0.0
  templates/                     Thymeleaf pages + fragments/layout.html
  static/css/style.css
deploy/
  bookstore.service              systemd unit
  deploy.sh                      install + firewall script
```
Books live in memory in `BookService`. To use a database, add `spring-boot-starter-data-jpa`
and a driver (H2/PostgreSQL) to `pom.xml` and swap the service for a repository.
