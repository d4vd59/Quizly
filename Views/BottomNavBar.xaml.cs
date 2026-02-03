using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class BottomNavBar : UserControl
    {
        private MainWindow _main;

        public BottomNavBar()
        {
            InitializeComponent();
        }

        public void SetMain(MainWindow main) => _main = main;

        private void Home_Click(object sender, System.Windows.RoutedEventArgs e) => _main.GoHome();
        private void Friends_Click(object sender, System.Windows.RoutedEventArgs e) => _main.GoFriends();
    }
}
