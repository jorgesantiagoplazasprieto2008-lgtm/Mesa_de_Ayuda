<%-- 
    Document   : index
    Created on : 10/08/2026, 1:22:24?p. m.
    Author     : Dissmax
--%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mesa de Ayuda CIMM - Login</title>
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #0a2b4e, #1a4f7a);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-card {
            max-width: 420px;
            width: 100%;
            border-radius: 1.5rem;
            background: #fff;
            box-shadow: 0 20px 60px rgba(0,0,0,0.4);
            padding: 2.5rem 2rem;
        }
        .login-card .logo {
            text-align: center;
            margin-bottom: 1.5rem;
        }
        .login-card .logo i {
            font-size: 3.5rem;
            color: #0a2b4e;
        }
        .login-card .logo h4 {
            font-weight: 700;
            color: #0a2b4e;
            margin-top: 0.5rem;
        }
        .login-card .logo p {
            color: #6c757d;
            font-size: 0.9rem;
        }
        .login-card .form-control {
            border-radius: 0.75rem;
            padding: 0.75rem 1rem;
            border: 1px solid #dee2e6;
        }
        .login-card .btn-login {
            background: #0a2b4e;
            color: #fff;
            border-radius: 0.75rem;
            padding: 0.75rem;
            font-weight: 600;
            transition: all 0.3s;
        }
        .login-card .btn-login:hover {
            background: #1a4f7a;
            transform: translateY(-2px);
        }
        .login-card .roles-hint {
            font-size: 0.8rem;
            color: #6c757d;
            text-align: center;
            margin-top: 1rem;
        }
        .login-card .roles-hint span {
            display: inline-block;
            background: #e9ecef;
            padding: 0.2rem 0.8rem;
            border-radius: 20px;
            margin: 0.2rem;
        }
    </style>
</head>
<body>
    <div class="login-card">
        <div class="logo">
            <i class="fas fa-headset"></i>
            <h4>Mesa de Ayuda CIMM</h4>
            <p>Gestión de tickets de soporte</p>
        </div>

        <form action="LoginServlet" method="post">
            <div class="mb-3">
                <label for="email" class="form-label"><i class="fas fa-envelope me-2"></i>Correo electrónico</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="usuario@cimm.edu.co" required>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label"><i class="fas fa-lock me-2"></i>Contraseña</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="????????" required>
            </div>
            <div class="mb-3 form-check">
                <input type="checkbox" class="form-check-input" id="remember">
                <label class="form-check-label" for="remember">Recordar sesión</label>
            </div>
            <button type="submit" class="btn btn-login w-100">
                <i class="fas fa-sign-in-alt me-2"></i>Ingresar
            </button>
        </form>

        <div class="roles-hint">
            <small>Credenciales de prueba:</small><br>
            <span><i class="fas fa-user me-1"></i>solicitante@cimm.edu</span>
            <span><i class="fas fa-user-tie me-1"></i>agente@cimm.edu</span>
            <span><i class="fas fa-user-cog me-1"></i>admin@cimm.edu</span>
        </div>

        <div class="text-center mt-3">
            <small class="text-muted">¿Olvidaste tu contraseña? <a href="#" class="text-primary">Recupérala aquí</a></small>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
