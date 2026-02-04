using System;
using System.Diagnostics;
using System.IO;
using Newtonsoft.Json;           // ✅ Für Serialize
using Newtonsoft.Json.Linq;      // ✅ Für JObject.Parse
using System.Threading.Tasks;
using System.Windows.Media.Imaging;
using Quizly.Extensions;

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

            using (var process = Process.Start(startInfo))
            {
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
        /// Loggt einen Benutzer aus
        /// </summary>
        public async Task<string> SignoutAsync(string user)
        {
            return await RunPythonAsync("signout", user);
        }

        /// <summary>
        /// Aktualisiert User-Profil
        /// </summary>
        /// <param name="user">User-ID, Nickname oder Email</param>
        /// <param name="currentPassword">Aktuelles Passwort (Pflicht!)</param>
        /// <param name="updates">Key=Value Paare für Updates (z.B. "nickname=newname")</param>
        public async Task<string> UpdateUserAsync(string user, string currentPassword, params string[] updates)
        {
            var args = new string[3 + updates.Length];
            args[0] = "update_user";
            args[1] = user;
            args[2] = currentPassword;
            Array.Copy(updates, 0, args, 3, updates.Length);
            
            return await RunPythonAsync(args);
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
        /// <param name="number">Optional: Anzahl zufällig ausgewählter Kategorien</param>
        public async Task<string> GetCategoriesAsync(int? number = null)
        {
            if (number.HasValue)
                return await RunPythonAsync("get_categories", number.Value.ToString());
            
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

        // ===== GAME MODES =====

        /// <summary>
        /// Holt alle verfügbaren Spiel-Modi
        /// </summary>
        public async Task<string> GetGameModesAsync()
        {
            return await RunPythonAsync("get_game_modes");
        }

        /// <summary>
        /// Holt einen spezifischen Game-Mode
        /// </summary>
        public async Task<string> GetGameModeAsync(int modeId)
        {
            return await RunPythonAsync("get_game_mode", modeId.ToString());
        }

        // ===== GAME STATES =====

        /// <summary>
        /// Holt alle verfügbaren Spiel-Zustände
        /// </summary>
        public async Task<string> GetGameStatesAsync()
        {
            return await RunPythonAsync("get_game_states");
        }

        /// <summary>
        /// Holt einen spezifischen Game-State
        /// </summary>
        public async Task<string> GetGameStateAsync(int stateId)
        {
            return await RunPythonAsync("get_game_state", stateId.ToString());
        }

        // ===== GAMEPLAY - UPDATED! =====

        /// <summary>
        /// Erstellt ein neues Single-Player-Spiel
        /// </summary>
        /// <param name="userId">User-ID</param>
        /// <param name="difficulty">Schwierigkeitsgrad (1-5)</param>
        /// <returns>JSON mit match_id</returns>
        public async Task<string> CreateSingleMatchAsync(int userId, int difficulty)
        {
            return await RunPythonAsync("create_single_match", userId.ToString(), difficulty.ToString());
        }

        /// <summary>
        /// Erstellt ein neues Multiplayer-Duell
        /// </summary>
        /// <param name="userId">User-ID</param>
        /// <param name="difficulty">Schwierigkeitsgrad (1-5)</param>
        /// <param name="opponentIds">Liste der Gegner-IDs</param>
        /// <returns>JSON mit match_id</returns>
        public async Task<string> CreateDuelMatchAsync(int userId, int difficulty, System.Collections.Generic.List<int> opponentIds)
        {
            // ✅ ÄNDERE: JsonSerializer → JsonConvert
            var opponentsJson = JsonConvert.SerializeObject(opponentIds);
            return await RunPythonAsync("create_duel_match", userId.ToString(), difficulty.ToString(), opponentsJson);
        }

        // ===== AVATAR MANAGEMENT =====

        /// <summary>
        /// Lädt einen Avatar hoch (aus lokalem Dateipfad)
        /// </summary>
        /// <param name="user">User-ID, Nickname oder Email</param>
        /// <param name="imagePath">Pfad zur Bilddatei</param>
        public async Task<string> UploadAvatarAsync(string user, string imagePath)
        {
            if (!File.Exists(imagePath))
                throw new FileNotFoundException($"Bild nicht gefunden: {imagePath}");

            return await RunPythonAsync("upload_avatar", user, imagePath);
        }

        /// <summary>
        /// Lädt Avatar herunter als Base64-JSON
        /// </summary>
        /// <param name="user">User-ID, Nickname oder Email</param>
        public async Task<string> GetAvatarJsonAsync(string user)
        {
            return await RunPythonAsync("get_avatar", user, "json");
        }

        /// <summary>
        /// Lädt Avatar herunter und konvertiert zu BitmapImage
        /// </summary>
        /// <param name="user">User-ID, Nickname oder Email</param>
        /// <returns>BitmapImage für WPF Image-Control</returns>
        public async Task<BitmapImage> GetAvatarImageAsync(string user)
        {
            try
            {
                string json = await GetAvatarJsonAsync(user);
                
                // ✅ ÄNDERE: JsonDocument → JObject
                var jObj = JObject.Parse(json);
                string base64Data = jObj["avatar"]?.ToString();
                
                if (string.IsNullOrEmpty(base64Data))
                    throw new Exception("Kein Avatar gefunden");
                
                byte[] imageBytes = Convert.FromBase64String(base64Data);

                var bitmap = new BitmapImage();
                using (var stream = new MemoryStream(imageBytes))
                {
                    bitmap.BeginInit();
                    bitmap.CacheOption = BitmapCacheOption.OnLoad;
                    bitmap.StreamSource = stream;
                    bitmap.EndInit();
                    bitmap.Freeze();
                }

                return bitmap;
            }
            catch (Exception ex)
            {
                throw new Exception($"Fehler beim Laden des Avatars: {ex.Message}");
            }
        }
    }
}
