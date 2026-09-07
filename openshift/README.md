# Ejercicio 2 — Despliegue en la Nube (PaaS)

> **Despliegue realizado (2026-09-03)**
> - PaaS: Red Hat OpenShift Service on AWS — Developer Sandbox
> - Proyecto: `juanpipitecnoinfo-dev`
> - Build: in-cluster, estrategia `docker`, entrada binaria (`Dockerfile` + `ej1.ear`)
> - URL: https://ej1-juanpipitecnoinfo-dev.apps.rm2.thpm.p1.openshiftapps.com/ej1-web/trabajadores
> - Verificado: alta (302), regla de negocio / matrícula repetida (HTTP 400), listar, buscar por matrícula.


Desplegar la imagen Docker del Ejercicio 1 en una PaaS. Acá se usa
**Developer Sandbox for Red Hat OpenShift** (gratis, sin tarjeta, renovable
cada 30 días — cuota: 3 cores / 14 GB RAM / 40 GB disco). La imagen se
construye **dentro del cluster**, así que no hace falta Docker local.

## 1. Requisitos

- Cuenta en el Sandbox: https://developers.redhat.com/developer-sandbox → *Start your sandbox*.
- CLI `oc`:
  - Windows: `winget install --id RedHat.OpenShift-Client`
    (o bajarlo de la consola web: `?` → *Command line tools*).
  - winget modifica el PATH: **cerrar y reabrir la terminal** después de instalar.
  - Verificar: `oc version --client`.
- El EAR compilado: `mvn clean install` en la raíz del proyecto.

## 2. Login

En la consola web del Sandbox: esquina superior derecha → tu usuario →
**Copy login command** → *Display token* → copiar la línea `oc login --token=... --server=...`
y pegarla en la terminal.

```bash
oc login --token=sha256~XXXX --server=https://api.<...>.openshiftapps.com:6443
oc project            # debería mostrar tu namespace  <usuario>-dev
```

## 3. Desplegar

```bash
./openshift/desplegar.sh
```

El script hace, de forma idempotente:

| Paso | Comando | Qué hace |
|---|---|---|
| 1 | `oc new-build --name ej1 --strategy docker --binary` | define un build que espera un contexto subido a mano |
| 2 | `oc start-build ej1 --from-dir . --follow` | sube `Dockerfile` + `ear/target/ej1.ear` (según `.dockerignore`) y construye la imagen con buildah en el cluster |
| 3 | `oc new-app ej1` | crea el `Deployment` + `Service` desde la imagen |
| 4 | `oc expose svc/ej1` + `oc patch route ... tls edge` | publica una URL HTTPS pública al puerto 8080 |

Al final imprime la URL. La app queda en:

```
https://ej1-<usuario>-dev.<cluster>.openshiftapps.com/ej1-web/trabajadores
```

## 4. Verificar

```bash
oc get pods                       # ej1-xxxx  Running / Ready 1/1
oc logs deploy/ej1 | tail -30     # "Deployed \"ej1.ear\"" y "WFLYSRV0025: ... started"
curl -k https://$(oc get route ej1 -o jsonpath='{.spec.host}')/ej1-web/trabajadores
```

Probar en el navegador: agregar un trabajador, listar, buscar por matrícula,
y que la matrícula repetida devuelva el error controlado (regla de negocio).

## 5. Volver a desplegar tras cambios

```bash
mvn clean install && oc start-build ej1 --from-dir . --follow
oc rollout status deploy/ej1
```

(OpenShift dispara el rollout solo al terminar el build, por el image trigger.)

## 6. Problemas comunes

| Síntoma | Causa / solución |
|---|---|
| `oc` no encontrado | instalar el CLI (paso 1) y reabrir la terminal |
| Build falla al bajar la imagen base | cambiar el tag en `Dockerfile` a `41.0.0.Final-jdk17` o `latest` |
| Pod en `CrashLoopBackOff`, log con `Permission denied` en `standalone/` | ya cubierto por el `chgrp 0 / chmod g+rwX` del `Dockerfile`; asegurate de haber reconstruido |
| Pod `OOMKilled` | `oc set resources deploy/ej1 --limits=memory=1500Mi` y/o `oc set env deploy/ej1 JAVA_OPTS_APPEND="-Xmx768m"` |
| La ruta da 503 unos segundos | WildFly todavía arrancando (~20-40 s); reintentar |
| Pod se apagó solo | el Sandbox mata los pods tras 12 h continuas: `oc rollout restart deploy/ej1` |

## 7. Limpiar

```bash
oc delete all -l app=ej1
oc delete bc/ej1 is/ej1
```

## Alternativas de PaaS (mismo `Dockerfile`)

- **Google Cloud Run** — `gcloud run deploy ej1 --source .` (build con Cloud Build). Necesita tarjeta; free tier amplio.
- **Fly.io** — `fly launch` detecta el `Dockerfile`. `fly deploy` para actualizar.
- **Koyeb / Render / Railway** — apuntan al repo + `Dockerfile`, deploy on push.
