using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;

namespace Quizly.Views
{
    public partial class CategoryPickView : UserControl
    {
        private readonly MainWindow _main;
        private List<Category> _allCategories;
        private List<Category> _selectedCategories;
        private bool _isPlayer1Turn = true; // Wechselt zwischen Spielern

        public CategoryPickView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
            
            InitializeCategories();
            LoadRandomCategories();
            UpdateCurrentPlayerDisplay();
        }

        private void InitializeCategories()
        {
            // Alle verfügbaren Kategorien
            _allCategories = new List<Category>
            {
                new Category { Name = "Macht & Geld", Description = "Politik und Wirtschaft", Icon = "💰" },
                new Category { Name = "Musik", Description = "Songs und Künstler", Icon = "🎵" },
                new Category { Name = "Gaming", Description = "Videospiele", Icon = "🕹" },
                new Category { Name = "Geschichte", Description = "Vergangene Ereignisse", Icon = "📜" },
                new Category { Name = "Sport", Description = "Sportarten und Events", Icon = "⚽" },
                new Category { Name = "Filme & Serien", Description = "Unterhaltung", Icon = "🎬" },
                new Category { Name = "Wissenschaft", Description = "Forschung und Technik", Icon = "🔬" },
                new Category { Name = "Geografie", Description = "Länder und Städte", Icon = "🌍" }
            };
        }

        private void LoadRandomCategories()
        {
            // 3 zufällige Kategorien auswählen
            Random random = new Random();
            _selectedCategories = _allCategories.OrderBy(x => random.Next()).Take(3).ToList();

            // Kategorien anzeigen
            Category1Name.Text = _selectedCategories[0].Name;
            Category1Desc.Text = _selectedCategories[0].Description;
            Category1Icon.Text = _selectedCategories[0].Icon;

            Category2Name.Text = _selectedCategories[1].Name;
            Category2Desc.Text = _selectedCategories[1].Description;
            Category2Icon.Text = _selectedCategories[1].Icon;

            Category3Name.Text = _selectedCategories[2].Name;
            Category3Desc.Text = _selectedCategories[2].Description;
            Category3Icon.Text = _selectedCategories[2].Icon;
        }

        private void UpdateCurrentPlayerDisplay()
        {
            if (_isPlayer1Turn)
            {
                CurrentPlayerAvatar.Fill = new SolidColorBrush(Color.FromRgb(77, 212, 232)); // Hellblau
                CurrentPlayerText.Text = "Du bist dran!";
            }
            else
            {
                CurrentPlayerAvatar.Fill = new SolidColorBrush(Color.FromRgb(77, 91, 158)); // Dunkelblau
                CurrentPlayerText.Text = "Gegner ist dran!";
            }
        }

        private void Category_Click(object sender, MouseButtonEventArgs e)
        {
            Border clickedBorder = sender as Border;
            if (clickedBorder == null) return;

            // Animation abspielen
            Storyboard animation = (Storyboard)this.Resources["SelectAnimation"];
            animation.Begin(clickedBorder);

            // Kurze Verzögerung für Animation, dann zur Frage navigieren
            System.Windows.Threading.DispatcherTimer timer = new System.Windows.Threading.DispatcherTimer();
            timer.Interval = TimeSpan.FromMilliseconds(300);
            timer.Tick += (s, args) =>
            {
                timer.Stop();
                
                // Kategorie-Index holen
                int categoryIndex = int.Parse(clickedBorder.Tag.ToString());
                Category selectedCategory = _selectedCategories[categoryIndex];

                // Feedback geben (optional: Sound, weitere Animation)
                AnimateSelection(clickedBorder);

                // Zur Frage navigieren
                System.Windows.Threading.DispatcherTimer delayTimer = new System.Windows.Threading.DispatcherTimer();
                delayTimer.Interval = TimeSpan.FromMilliseconds(400);
                delayTimer.Tick += (s2, e2) =>
                {
                    delayTimer.Stop();
                    _main.NavigateTo(new QuestionView(_main));
                };
                delayTimer.Start();
            };
            timer.Start();
        }

        private void AnimateSelection(Border border)
        {
            // Grünes Highlight-Animation
            ColorAnimation colorAnim = new ColorAnimation
            {
                To = Color.FromRgb(76, 175, 80), // Grün
                Duration = TimeSpan.FromMilliseconds(200),
                AutoReverse = true
            };

            SolidColorBrush brush = new SolidColorBrush(Color.FromArgb(51, 255, 255, 255));
            border.Background = brush;
            brush.BeginAnimation(SolidColorBrush.ColorProperty, colorAnim);
        }

        private void Back_Click(object sender, RoutedEventArgs e)
        {
            // ✅ Leer gelassen - Zurück-Navigation nicht erlaubt
            // Der Button ist sowieso ausgeblendet (Visibility="Collapsed")
        }
    }

    // Hilfsklasse für Kategorien
    public class Category
    {
        public string Name { get; set; }
        public string Description { get; set; }
        public string Icon { get; set; }
    }
}
