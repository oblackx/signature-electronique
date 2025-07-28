server.js
require('dotenv').config();
const express = require('express');
const bodyParser = require('body-parser');
const mysql = require('mysql2/promise');
const bcrypt = require('bcrypt');
const cors = require('cors');

const app = express();
const port = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());

// Configuration de la base de données (à adapter selon votre .env)
const dbConfig = {
    host: process.env.DB_HOST || 'localhost',
    user: process.env.DB_USER || 'root',
    password: process.env.DB_PASSWORD || '',
    database: process.env.DB_NAME || 'uiz_esign'
};

// Connexion à MySQL
let pool;
async function initDb() {
    pool = mysql.createPool(dbConfig);
    console.log('Connecté à la base de données MySQL');
}

// Routes
app.post('/api/auth/login', async (req, res) => {
    try {
        const { email, password } = req.body;
        const [users] = await pool.query('SELECT * FROM users WHERE email = ?', [email]);
        
        if (users.length === 0) {
            return res.status(401).json({ success: false, message: "Email incorrect" });
        }

        const user = users[0];
        const match = await bcrypt.compare(password, user.password);
        
        if (match) {
            res.json({ success: true, user: { id: user.id, nom: user.nom, email: user.email } });
        } else {
            res.status(401).json({ success: false, message: "Mot de passe incorrect" });
        }
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Erreur serveur" });
    }
});

// Démarrer le serveur
initDb().then(() => {
    app.listen(port, () => {
        console.log(`API en écoute sur http://localhost:${port}`);
    });
});