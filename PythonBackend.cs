using System;
using System.Diagnostics;
using System.Text.Json;
using System.Threading.Tasks;

namespace Quizly
{
    /// <summary>
    /// Python Backend Client - Ruft das Python Backend auf
    /// 
    /// Verwendung:
    ///     var backend = new PythonBackend();
    ///     string json = await backend.GetCategories();
    ///     var categories = JsonDocument.Parse(json);
    /// </summary>
    public class PythonBackend
    {
        private readonly string pythonPath;
        private readonly string scriptPath;

        public PythonBackend()
        {
            // Python-Pfad (kann angepasst werden)
            pythonPath = "python";
            
            // Pfad zum Python-Script (relativ zum Projekt)
            scriptPath = @"python-backend\src\main.py";
        }

        /// <summary>
        /// Führt einen Python-Command aus und gibt JSON zurück
        /// </summary>
        private async Task<string> RunPythonAsync(params string[] args)
        {
            var arguments = $"\"{scriptPath}\" {string.Join(" ", args)}";

            var startInfo = new ProcessStartInfo
            {
                FileName = pythonPath,
                Arguments = arguments,
                RedirectStandardOutput = true,
                RedirectStandardError = true,
                UseShellExecute = false,
                CreateNoWindow = true
            };

            using var process = Process.Start(startInfo);
            
            if (process == null)
                throw new Exception("Python-Prozess konnte nicht gestartet werden");

            string output = await process.StandardOutput.ReadToEndAsync();
            string error = await process.StandardError.ReadToEndAsync();
            
            await process.WaitForExitAsync();

            if (process.ExitCode != 0)
            {
                throw new Exception($"Python-Fehler: {error}");
            }

            return output;
        }

        // ===== USER MANAGEMENT =====

        /// <summary>
        /// Registriert einen neuen Benutzer
        /// </summary>
        public async Task<string> SignupAsync(string email, string nickname, string fullname, string password)
        {
            return await RunPythonAsync("signup", email, nickname, fullname, password);
        }

        /// <summary>
        /// Loggt einen Benutzer ein
        /// </summary>
        public async Task<string> SigninAsync(string user, string password)
        {
            return await RunPythonAsync("signin", user, password);
        }

        /// <summary>
        /// Holt Liste aller User
        /// </summary>
        public async Task<string> GetUsersAsync()
        {
            return await RunPythonAsync("get_users");
        }

        /// <summary>
        /// Holt Details für einen User
        /// </summary>
        public async Task<string> GetUserAsync(string user)
        {
            return await RunPythonAsync("get_user", user);
        }

        // ===== KATEGORIEN =====

        /// <summary>
        /// Holt alle Kategorien
        /// </summary>
        public async Task<string> GetCategoriesAsync()
        {
            return await RunPythonAsync("get_categories");
        }

        /// <summary>
        /// Holt eine spezifische Kategorie
        /// </summary>
        public async Task<string> GetCategoryAsync(int categoryId)
        {
            return await RunPythonAsync("get_category", categoryId.ToString());
        }

        // ===== SCHWIERIGKEITSGRADE =====

        /// <summary>
        /// Holt alle Schwierigkeitsgrade
        /// </summary>
        public async Task<string> GetDifficultiesAsync()
        {
            return await RunPythonAsync("get_difficulties");
        }

        /// <summary>
        /// Holt einen spezifischen Schwierigkeitsgrad
        /// </summary>
        public async Task<string> GetDifficultyAsync(int difficultyId)
        {
            return await RunPythonAsync("get_difficulty", difficultyId.ToString());
        }

        // ===== SETTINGS =====

        /// <summary>
        /// Holt die Spieleinstellungen
        /// </summary>
        public async Task<string> GetSettingsAsync()
        {
            return await RunPythonAsync("get_settings");
        }
    }

    // ===== BEISPIEL-VERWENDUNG =====
    
    /* 
    // In deinem MainWindow.xaml.cs oder einer anderen Klasse:
    
    public partial class MainWindow : Window
    {
        private PythonBackend backend;

        public MainWindow()
        {
            InitializeComponent();
            backend = new PythonBackend();
        }

        private async void LoadCategories_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                // Kategorien von Python Backend holen
                string json = await backend.GetCategoriesAsync();
                
                // JSON parsen
                var doc = JsonDocument.Parse(json);
                
                // Kategorien anzeigen
                foreach (var category in doc.RootElement.EnumerateArray())
                {
                    int id = category.GetProperty("id").GetInt32();
                    string name = category.GetProperty("name").GetString();
                    
                    Console.WriteLine($"Kategorie {id}: {name}");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Fehler: {ex.Message}");
            }
        }

        private async void Signup_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                string result = await backend.SignupAsync(
                    "test@test.de",
                    "nickname",
                    "Max Mustermann",
                    "Pass123"
                );
                
                var doc = JsonDocument.Parse(result);
                string message = doc.RootElement.GetProperty("Message").GetString();
                
                MessageBox.Show(message);
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Fehler: {ex.Message}");
            }
        }
    }
    */
}
