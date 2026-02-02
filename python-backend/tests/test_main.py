import unittest
from src.main import app  # Assuming 'app' is the main application instance

class MainAppTests(unittest.TestCase):

    def setUp(self):
        self.app = app.test_client()
        self.app.testing = True

    def test_home_page(self):
        response = self.app.get('/')
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'Welcome', response.data)  # Adjust based on actual content

    # Add more tests as needed

if __name__ == '__main__':
    unittest.main()