using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class LoginView : UserControl
    {
        private readonly MainWindow _main;

        public LoginView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void Login_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.GoHome();
        }

        private void Register_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.GoHome();
        }

        private void Guest_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.GoHome();
        }
    }
}
