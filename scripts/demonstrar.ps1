$ErrorActionPreference = 'Stop'
$base = 'http://localhost:8080'
$runId = [DateTimeOffset]::UtcNow.ToUnixTimeMilliseconds().ToString()
function Enviar($metodo,$rota,$corpo,$esperado) {
    $argsHttp = @{ Uri="$base$rota"; Method=$metodo; UseBasicParsing=$true }
    if ($null -ne $corpo) { $argsHttp.ContentType='application/json'; $argsHttp.Body=($corpo | ConvertTo-Json -Compress) }
    try { $r=Invoke-WebRequest @argsHttp; $status=[int]$r.StatusCode; $conteudo=$r.Content }
    catch {
        if ($null -eq $_.Exception.Response) { throw }
        $status=[int]$_.Exception.Response.StatusCode; $conteudo=$_.ErrorDetails.Message
    }
    if ($status -ne $esperado) { throw "$metodo $rota retornou $status; esperado $esperado. $conteudo" }
    Write-Host "$metodo $rota -> $status (OK)" -ForegroundColor Green
    if ($conteudo -and $conteudo.StartsWith('{')) { return ($conteudo | ConvertFrom-Json) }
}
Enviar GET '/api/health' $null 200 | Out-Null
$grupo=Enviar POST '/api/grupos-produtos' @{nome="Perifericos $runId"} 201
$fornecedor=Enviar POST '/api/fornecedores' @{razaoSocial="Distribuidora ficticia $runId";cnpj="0$runId"} 201
$produto=@{codigoBarras="INF-$runId";descricao='Mouse USB demonstracao';saldoEstoque=10;valorUnitario=59.90;estoqueMinimo=2;grupoId=$grupo.id;fornecedorId=$fornecedor.id;garantiaMeses=12}
$salvo=Enviar POST '/api/produtos' $produto 201
$consulta=Enviar GET "/api/produtos/$($salvo.id)" $null 200
if ($consulta.garantiaMeses -ne 12 -or $consulta.valorEstoque -ne 599) { throw 'Valores consultados divergentes.' }
Enviar GET '/api/produtos' $null 200 | Out-Null
Enviar POST '/api/produtos' $produto 409 | Out-Null
$produto.saldoEstoque=-1
Enviar POST '/api/produtos' $produto 400 | Out-Null
Enviar GET '/api/produtos/9223372036854775807' $null 404 | Out-Null
$produto.saldoEstoque=10; $produto.garantiaMeses=61
Enviar POST '/api/produtos' $produto 400 | Out-Null
Write-Host "Produto demonstrado: ID $($salvo.id), codigo INF-$runId" -ForegroundColor Cyan
Write-Host 'Todos os cenarios HTTP passaram. Os registros ficticios permanecem no banco dev.'
