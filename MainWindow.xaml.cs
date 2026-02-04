using System.Windows;
using Quizly.Views;

namespace Quizly
{
    public partial class MainWindow : Window
    {
        public GameState CurrentGameState { get; private set; }

        public MainWindow()
        {
            InitializeComponent();
            BottomBar.SetMain(this);
            CurrentGameState = new GameState();

            // Login ohne BottomBar
            ShowBottomBar(false);
            NavigateTo(new LoginView(this));
        }

        public void NavigateTo(object view) => MainContent.Content = view;

        public void ShowBottomBar(bool visible)
        {
            BottomBar.Visibility = visible ? Visibility.Visible : Visibility.Collapsed;
        }

        public void GoHome()
        {
            ShowBottomBar(true);  // Nur hier BottomBar anzeigen
            NavigateTo(new HomeView(this));
        }

        public void GoFriends()
        {
            ShowBottomBar(true);  // Nur hier BottomBar anzeigen
            NavigateTo(new FriendsView(this));
        }

        public void GoLogin()
        {
            ShowBottomBar(false);
            NavigateTo(new LoginView(this));
        }

        public void StartNewGame(string opponentName)
        {
            CurrentGameState.Reset();
            CurrentGameState.OpponentName = opponentName;
        }
    }
}
