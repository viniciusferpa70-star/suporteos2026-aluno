$ErrorActionPreference = 'Stop'
Set-Location (Split-Path $PSScriptRoot -Parent)
if (!(Test-Path .env)) { throw 'Crie .env a partir de .env.example e configure as senhas locais.' }
try {
    $health = Invoke-WebRequest 'http://localhost:8080/api/health' -TimeoutSec 2 -UseBasicParsing
    if ($health.Content -eq 'OK') {
        Write-Host 'Aplicacao ja esta funcionando em http://localhost:8080' -ForegroundColor Green
        return
    }
} catch { }
& .\mvnw.cmd spring-boot:run '-Dspring-boot.run.profiles=dev'
if ($LASTEXITCODE -ne 0) { throw 'Falha ao iniciar; consulte a mensagem acima.' }
