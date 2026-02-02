using System.Windows;
using System.Windows.Controls;
using Quizly.Views;


namespace Quizly
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();

            // Startscreen anzeigen
            NavigateTo(new StartView(this));
        }

        public void NavigateTo(object view) => MainContent.Content = view;

    }
}
