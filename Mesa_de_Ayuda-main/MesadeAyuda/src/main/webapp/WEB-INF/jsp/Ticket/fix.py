import os
import re

files = ["../Dashboard.jsp", "list.jsp", "detail.jsp", "create.jsp"]
c_if_start = '<c:if test="">'
c_if_end = '</c:if>'

for f in files:
    if not os.path.exists(f):
        continue
    with open(f, 'r', encoding='utf-8') as file:
        content = file.read()
    
    # regex to find the li tag with Nuevo Ticket or Crear Ticket
    pattern = re.compile(r'(<c:if test="\$\{usuarioLogueado\.rol == \'SOLICITANTE\'\}">\s*)*(<li class="nav-item">\s*<a class="nav-link[^>]*href="[^"]*action=create[^>]*>.*?(Nuevo Ticket|Crear Ticket).*?</a>\s*</li>)(\s*</c:if>)*', re.DOTALL)
    
    content = pattern.sub(r'<c:if test="">\n\2\n</c:if>', content)
    
    with open(f, 'w', encoding='utf-8') as file:
        file.write(content)
