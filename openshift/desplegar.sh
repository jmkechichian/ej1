#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# Despliegue de ej1 en OpenShift (Developer Sandbox) usando la imagen Docker.
#
# La imagen se construye DENTRO del cluster (build "binary" con estrategia
# docker), asi que NO hace falta tener Docker instalado en la maquina local:
# solo el CLI 'oc' y el EAR ya compilado.
#
# Uso:
#   1) mvn clean install                 (genera ear/target/ej1.ear)
#   2) oc login ...                      (copiar el comando del boton
#                                         "Copy login command" del sandbox)
#   3) ./openshift/desplegar.sh
# ---------------------------------------------------------------------------
set -euo pipefail

APP=ej1
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

if [ ! -f ear/target/ej1.ear ]; then
  echo "ERROR: falta ear/target/ej1.ear -- corre 'mvn clean install' primero." >&2
  exit 1
fi

echo ">> Proyecto actual: $(oc project -q)"

# 1) Definir el build (idempotente): estrategia docker, entrada binaria.
if ! oc get bc "$APP" >/dev/null 2>&1; then
  oc new-build --name "$APP" --strategy docker --binary
fi

# 2) Subir el contexto (Dockerfile + ear/target/ej1.ear segun .dockerignore)
#    y construir la imagen en el cluster.
oc start-build "$APP" --from-dir . --follow

# 3) Crear el Deployment + Service a partir de la imagen recien construida.
if ! oc get deploy "$APP" >/dev/null 2>&1; then
  oc new-app "$APP"
  # WildFly tarda en arrancar: dar aire a los health checks por defecto.
  oc set resources deploy/"$APP" --requests=memory=768Mi --limits=memory=1200Mi
fi

# 4) Publicar una URL HTTPS externa hacia el puerto 8080.
oc expose svc/"$APP" >/dev/null 2>&1 || true
oc patch route/"$APP" -p '{"spec":{"tls":{"termination":"edge"}}}' >/dev/null 2>&1 || true

echo ">> Esperando a que el pod este listo..."
oc rollout status deploy/"$APP" --timeout=180s || true

URL="https://$(oc get route "$APP" -o jsonpath='{.spec.host}')"
echo
echo "==================================================================="
echo " App:      $URL/ej1-web/trabajadores"
echo " Bienvenida WildFly: $URL/"
echo "==================================================================="
