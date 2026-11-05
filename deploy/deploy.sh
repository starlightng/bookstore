#!/usr/bin/env bash
# Installs the built jar as a systemd service and opens the firewall port.
# Usage (from project root, after `mvn clean package`):
#   sudo ./deploy/deploy.sh            # uses port 8088
#   sudo PORT=9090 ./deploy/deploy.sh  # custom port
set -euo pipefail

PORT="${PORT:-8088}"
APP_DIR=/opt/bookstore
JAR="$(ls target/bookstore-*.jar | grep -v original | head -n1)"

[ -f "$JAR" ] || { echo "Jar not found. Run 'mvn clean package' first."; exit 1; }
[ "$(id -u)" -eq 0 ] || { echo "Run as root (sudo)."; exit 1; }

id bookstore &>/dev/null || useradd --system --no-create-home --shell /usr/sbin/nologin bookstore
mkdir -p "$APP_DIR"
cp "$JAR" "$APP_DIR/bookstore.jar"
chown -R bookstore:bookstore "$APP_DIR"

sed "s/SERVER_PORT=8088/SERVER_PORT=${PORT}/" deploy/bookstore.service > /etc/systemd/system/bookstore.service
systemctl daemon-reload
systemctl enable --now bookstore

# Open the port in whichever firewall is active
if command -v ufw &>/dev/null && ufw status | grep -q "Status: active"; then
    ufw allow "${PORT}/tcp"
elif command -v firewall-cmd &>/dev/null && systemctl is-active --quiet firewalld; then
    firewall-cmd --permanent --add-port="${PORT}/tcp"
    firewall-cmd --reload
else
    echo "No active ufw/firewalld found. If you use iptables, run:"
    echo "  iptables -I INPUT -p tcp --dport ${PORT} -j ACCEPT"
fi

echo "Done. App should be reachable at http://<server-ip>:${PORT}"
echo "Logs: journalctl -u bookstore -f"
