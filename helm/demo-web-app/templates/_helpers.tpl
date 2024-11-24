
{{- define "demo-web-app.name" -}}
demo-web-app
{{- end -}}

{{- define "demo-web-app.fullname" -}}
{{- .Release.Name }}
{{- end -}}

{{- define "demo-web-app.labels" -}}
app.kubernetes.io/name: {{ include "demo-web-app.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}