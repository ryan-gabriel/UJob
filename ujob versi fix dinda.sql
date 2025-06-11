-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 11, 2025 at 01:11 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ujob`
--

-- --------------------------------------------------------

--
-- Table structure for table `anggota_proyek`
--

CREATE TABLE `anggota_proyek` (
  `id` int(11) NOT NULL,
  `proyek_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `role` varchar(50) DEFAULT 'anggota'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `anggota_proyek`
--

INSERT INTO `anggota_proyek` (`id`, `proyek_id`, `user_id`, `role`) VALUES
(1, 1, 2, 'anggota'),
(2, 1, 3, 'anggota'),
(3, 2, 3, 'anggota'),
(4, 4, 4, 'anggota'),
(5, 5, 5, 'anggota');

-- --------------------------------------------------------

--
-- Table structure for table `lamaran`
--

CREATE TABLE `lamaran` (
  `lamaran_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `lowongan_id` int(11) NOT NULL,
  `tanggal_lamar` date DEFAULT curdate(),
  `status_lamaran` enum('dikirim','diproses','diterima','ditolak') DEFAULT 'dikirim'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `lamaran`
--

INSERT INTO `lamaran` (`lamaran_id`, `user_id`, `lowongan_id`, `tanggal_lamar`, `status_lamaran`) VALUES
(1, 1, 6, '2025-06-10', 'dikirim'),
(2, 2, 6, '2025-06-10', 'diproses'),
(3, 3, 7, '2025-06-10', 'ditolak'),
(4, 4, 8, '2025-06-09', 'diterima'),
(5, 5, 6, '2025-06-08', 'diproses');

-- --------------------------------------------------------

--
-- Table structure for table `log_aktivitas`
--

CREATE TABLE `log_aktivitas` (
  `log_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `aktivitas` text NOT NULL,
  `waktu` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `log_aktivitas`
--

INSERT INTO `log_aktivitas` (`log_id`, `user_id`, `aktivitas`, `waktu`) VALUES
(1, 1, 'Login ke sistem', '2025-06-10 01:00:00'),
(2, 1, 'Melamar pekerjaan Intern Web Developer (lowongan_id 6)', '2025-06-10 01:05:00'),
(3, 2, 'Upload portofolio baru', '2025-06-10 02:00:00'),
(4, 3, 'Mengajukan diri ke proyek Marketplace Produk Lokal (proyek_id 4)', '2025-06-10 02:15:00'),
(5, 4, 'Melamar pekerjaan Content Creator (lowongan_id 8)', '2025-06-09 09:40:00'),
(6, 14, 'Perusahaan membuat lowongan Intern Web Developer', '2025-06-09 03:30:00'),
(7, 15, 'Login perusahaan', '2025-06-09 04:00:00'),
(8, 16, 'Update deskripsi perusahaan', '2025-06-09 05:45:00');

-- --------------------------------------------------------

--
-- Table structure for table `lowongan`
--

CREATE TABLE `lowongan` (
  `lowongan_id` int(11) NOT NULL,
  `judul_pekerjaan` varchar(100) DEFAULT NULL,
  `deskripsi` text DEFAULT NULL,
  `kualifikasi` text DEFAULT NULL,
  `tanggal_posting` date DEFAULT curdate(),
  `status` enum('aktif','nonaktif') DEFAULT 'aktif',
  `tanggal_deadline` date DEFAULT NULL,
  `jenis_lowongan` varchar(100) DEFAULT NULL,
  `gaji` varchar(100) DEFAULT NULL,
  `lokasi_kerja` varchar(100) DEFAULT NULL,
  `durasi` varchar(100) DEFAULT NULL,
  `jumlah_lamaran` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `lowongan`
--

INSERT INTO `lowongan` (`lowongan_id`, `judul_pekerjaan`, `deskripsi`, `kualifikasi`, `tanggal_posting`, `status`, `tanggal_deadline`, `jenis_lowongan`, `gaji`, `lokasi_kerja`, `durasi`, `jumlah_lamaran`, `user_id`) VALUES
(2, 'Backend Developer', 'Mengembangkan REST API menggunakan Node.js', 'Minimal pengalaman 2 tahun, paham database relasional', '2025-06-11', 'nonaktif', '2025-06-24', 'Magang', '10-12 juta', 'Jakarta', '12 bulan', 8, 13),
(3, 'UI/UX Designer', 'Merancang antarmuka aplikasi mobile dan web', 'Menguasai Figma, Adobe XD, dan prinsip desain', '2025-06-11', 'aktif', '2025-06-30', 'Freelance', '5-7 juta', 'Remote', '3 bulan', 5, 13),
(4, 'QA Tester', 'Melakukan testing manual dan otomatis pada aplikasi', 'Paham tools seperti Postman dan Selenium', '2025-06-11', 'aktif', '2025-07-10', 'Kontrak', '6-8 juta', 'Surabaya', '6 bulan', 9, 13),
(5, 'DevOps Engineer', 'Mengelola deployment dan monitoring aplikasi', 'Menguasai CI/CD, Docker, dan Kubernetes', '2025-06-11', 'aktif', '2025-07-05', 'Full Time', '12-15 juta', 'Yogyakarta', '12 bulan', 4, 13),
(6, 'Intern Web Developer', 'Magang untuk pengembangan front-end', 'Menguasai HTML, CSS, JavaScript', '2025-06-10', 'aktif', '2025-07-01', 'Magang', '3-5 juta', 'Remote', '3 bulan', NULL, NULL),
(7, 'Data Analyst', 'Analisis data penjualan dan pelanggan', 'Mahasiswa tingkat akhir jurusan Statistik / TI', '2025-06-10', 'aktif', '2025-06-25', 'Paruh waktu', '5-7 juta', 'Bandung', '6 bulan', NULL, NULL),
(8, 'Content Creator', 'Buat konten sosial media perusahaan', 'Kreatif, paham tren digital', '2025-06-10', 'aktif', '2025-07-15', 'Full Time', '5-8 juta', 'Jakarta', '1 tahun', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `minat_mahasiswa`
--

CREATE TABLE `minat_mahasiswa` (
  `minat_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `minat` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `minat_mahasiswa`
--

INSERT INTO `minat_mahasiswa` (`minat_id`, `user_id`, `minat`) VALUES
(1, 1, 'Pengembangan Web'),
(2, 1, 'UI/UX Design'),
(3, 2, 'Data Science'),
(4, 2, 'Statistik'),
(5, 3, 'Desain Grafis'),
(6, 3, 'Branding'),
(7, 4, 'Content Writing'),
(8, 5, 'Keuangan'),
(9, 5, 'Investasi'),
(10, 6, 'Teknologi Pendidikan'),
(11, 7, 'Sosial Media'),
(12, 8, 'Machine Learning'),
(13, 9, 'Psikologi'),
(14, 10, 'Mobile Development'),
(15, 11, 'Cybersecurity'),
(16, 12, 'Pendidikan Anak'),
(17, 13, 'Kewirausahaan');

-- --------------------------------------------------------

--
-- Table structure for table `notifikasi`
--

CREATE TABLE `notifikasi` (
  `notif_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `isi` text DEFAULT NULL,
  `waktu` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `notifikasi`
--

INSERT INTO `notifikasi` (`notif_id`, `user_id`, `isi`, `waktu`) VALUES
(1, 1, 'Lamaran Anda untuk Intern Web Developer telah dikirim.', '2025-06-10 01:05:00'),
(2, 2, 'Lamaran Anda sedang diproses oleh PT Abadi Jaya.', '2025-06-10 02:00:00'),
(3, 3, 'Lamaran Anda untuk Data Analyst ditolak.', '2025-06-10 02:30:00'),
(4, 4, 'Selamat! Anda diterima pada lowongan Content Creator.', '2025-06-09 10:00:00'),
(5, 5, 'Anda telah bergabung ke proyek Sistem E-Absensi.', '2025-06-09 03:20:00'),
(6, 1, 'Lowongan baru: Mobile Developer dari PT Mandiri Utama.', '2025-06-10 00:45:00'),
(7, 1, 'Anda melakukan pendaftaran ke proyek \'Marketplace Produk Lokal\'.', '2025-06-10 12:01:01'),
(8, 3, 'dinda mendaftar ke proyek anda dengan judul \'Marketplace Produk Lokal\'.', '2025-06-10 12:01:01');

-- --------------------------------------------------------

--
-- Table structure for table `pengajuan_proyek`
--

CREATE TABLE `pengajuan_proyek` (
  `pengajuan_id` int(11) NOT NULL,
  `proyek_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `pesan` text DEFAULT NULL,
  `tanggal_pengajuan` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` enum('menunggu','diterima','ditolak') DEFAULT 'menunggu'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengajuan_proyek`
--

INSERT INTO `pengajuan_proyek` (`pengajuan_id`, `proyek_id`, `user_id`, `pesan`, `tanggal_pengajuan`, `status`) VALUES
(1, 1, 4, 'Saya tertarik untuk ikut bantu desain UI.', '2025-06-10 06:33:23', 'menunggu'),
(2, 3, 5, 'Saya bisa bantu backend dan dokumentasi.', '2025-06-10 06:33:23', 'diterima'),
(3, 4, 6, 'Ingin belajar langsung di proyek ini.', '2025-06-10 06:33:23', 'menunggu'),
(4, 4, 1, NULL, '2025-06-10 12:01:01', 'menunggu');

-- --------------------------------------------------------

--
-- Table structure for table `perusahaan`
--

CREATE TABLE `perusahaan` (
  `perusahaan_id` int(11) NOT NULL,
  `user_id` int(5) NOT NULL,
  `jenis_industri` varchar(100) DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `email_kontak` varchar(150) DEFAULT NULL,
  `website_resmi` varchar(255) DEFAULT NULL,
  `jumlah_koneksi` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `jumlah_karyawan` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `deskripsi_perusahaan` text DEFAULT NULL,
  `kategori_produk_layanan` varchar(255) DEFAULT NULL,
  `informasi_kontak_lainnya` text DEFAULT NULL,
  `deskripsi` text DEFAULT NULL,
  `website` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `perusahaan`
--

INSERT INTO `perusahaan` (`perusahaan_id`, `user_id`, `jenis_industri`, `alamat`, `email_kontak`, `website_resmi`, `jumlah_koneksi`, `jumlah_karyawan`, `deskripsi_perusahaan`, `kategori_produk_layanan`, `informasi_kontak_lainnya`, `deskripsi`, `website`) VALUES
(1, 13, 'Teknologi', 'Jl. Merdeka No. 45, Jakarta', 'contact@abadi.jaya.com', 'www.abadi.jaya.com', 100, 500, 'PT Abadi Jaya bergerak di bidang teknologi informasi, menawarkan solusi IT untuk berbagai perusahaan.', 'Software, IT Consulting', '0800-1234-5678', 'Perusahaan dengan pengalaman lebih dari 10 tahun.', 'www.abadi.jaya.com'),
(2, 15, 'Manufaktur', 'Jl. Makmur No. 10, Surabaya', 'contact@sumber.makmur.com', 'www.sumber.makmur.com', 80, 300, 'PT Sumber Makmur merupakan produsen barang elektronik terkemuka di Indonesia.', 'Elektronik, Pabrikasi', '0811-2345-6789', 'Perusahaan dengan jaringan distribusi global.', 'www.sumber.makmur.com'),
(3, 16, 'Konstruksi', 'Jl. Sejahtera No. 88, Bandung', 'contact@sejahtera.abadi.com', 'www.sejahtera.abadi.com', 60, 200, 'PT Sejahtera Abadi adalah perusahaan konstruksi yang fokus pada proyek-proyek infrastruktur besar.', 'Konstruksi, Infrastruktur', '0822-3456-7890', 'Memiliki proyek di beberapa kota besar di Indonesia.', 'www.sejahtera.abadi.com'),
(4, 17, 'Perdagangan', 'Jl. Karya No. 12, Bali', 'contact@karya.bersama.com', 'www.karya.bersama.com', 120, 350, 'PT Karya Bersama adalah perusahaan distribusi yang melayani berbagai sektor industri.', 'Perdagangan, Distribusi', '0833-4567-8901', 'Melayani klien dari berbagai sektor industri di Indonesia.', 'www.karya.bersama.com'),
(5, 18, 'Keuangan', 'Jl. Global No. 25, Jakarta', 'contact@global.solusi.com', 'www.global.solusi.com', 150, 400, 'PT Global Solusi menyediakan layanan keuangan untuk bisnis dan individu.', 'Layanan Keuangan', '0855-5678-9012', 'Layanan perbankan dan investasi untuk perusahaan dan individu.', 'www.global.solusi.com'),
(6, 19, 'Pertanian', 'Jl. Alam No. 60, Yogyakarta', 'contact@indah.alam.com', 'www.indah.alam.com', 90, 150, 'PT Indah Alam adalah perusahaan yang bergerak di sektor pertanian dengan fokus pada hasil pertanian organik.', 'Pertanian Organik', '0866-6789-0123', 'Menyediakan produk pertanian berkualitas tinggi.', 'www.indah.alam.com'),
(7, 20, 'Retail', 'Jl. Mandiri No. 5, Medan', 'contact@mandiri.utama.com', 'www.mandiri.utama.com', 200, 500, 'PT Mandiri Utama adalah perusahaan retail besar yang menyediakan berbagai produk untuk kebutuhan sehari-hari.', 'Retail, Supermarket', '0877-7890-1234', 'Supermarket dengan cabang di seluruh Indonesia.', 'www.mandiri.utama.com');

-- --------------------------------------------------------

--
-- Table structure for table `portofolio`
--

CREATE TABLE `portofolio` (
  `porto_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `judul` varchar(100) DEFAULT NULL,
  `jenis` varchar(120) NOT NULL,
  `deskripsi` text DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `tanggal_upload` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `portofolio`
--

INSERT INTO `portofolio` (`porto_id`, `user_id`, `judul`, `jenis`, `deskripsi`, `link`, `tanggal_upload`) VALUES
(1, 1, 'Proyek Aplikasi Mobile', '0', 'Membuat aplikasi mobile berbasis Android', 'http://link-portfolio.com/proyek-a', '2025-06-09 08:08:18'),
(2, 1, 'Website E-Commerce', '0', 'Membuat website e-commerce menggunakan Laravel', 'http://link-portfolio.com/proyek-b', '2025-06-09 08:08:18'),
(3, 3, 'Sistem Manajemen Data', '0', 'Membuat sistem manajemen data untuk perusahaan', 'http://link-portfolio.com/proyek-c', '2025-06-09 08:08:18'),
(4, 4, 'Aplikasi Web', '0', 'Membuat aplikasi berbasis web untuk manajemen inventaris', 'http://link-portfolio.com/proyek-d', '2025-06-09 08:08:18'),
(5, 5, 'Proyek IoT', '0', 'Mengembangkan proyek Internet of Things dengan sensor', 'http://link-portfolio.com/proyek-e', '2025-06-09 08:08:18'),
(6, 6, 'Platform Pendidikan', '0', 'Membangun platform pendidikan untuk pembelajaran online', 'http://link-portfolio.com/proyek-f', '2025-06-09 08:08:18'),
(7, 7, 'E-Learning System', '0', 'Membuat sistem e-learning untuk universitas', 'http://link-portfolio.com/proyek-g', '2025-06-09 08:08:18'),
(8, 8, 'Sistem Rekrutmen', '0', 'Membangun sistem rekrutmen berbasis web untuk perusahaan', 'http://link-portfolio.com/proyek-h', '2025-06-09 08:08:18'),
(9, 9, 'Aplikasi Keuangan', '0', 'Mengembangkan aplikasi untuk pengelolaan keuangan pribadi', 'http://link-portfolio.com/proyek-i', '2025-06-09 08:08:18'),
(10, 10, 'Sistem CRM', '0', 'Membangun sistem CRM untuk perusahaan retail', 'http://link-portfolio.com/proyek-j', '2025-06-09 08:08:18'),
(11, 1, 'UI/UX Redesign Aplikasi', 'Inovasi', 'Redesign tampilan aplikasi belajar', 'http://portfolio.com/uiux', '2025-06-10 06:32:26'),
(12, 2, 'Machine Learning Project', '0', 'Model klasifikasi sentimen produk', 'http://portfolio.com/mlproject', '2025-06-10 06:32:26'),
(13, 3, 'Website Alumni', '0', 'Web alumni kampus dengan fitur posting dan event', 'http://portfolio.com/alumni', '2025-06-10 06:32:26'),
(14, 3, 'Poster Digital', '0', 'Kumpulan desain poster digital untuk kampus', 'http://portfolio.com/poster', '2025-06-10 06:32:26'),
(15, 1, 'wejhdOAISHUASH', 'Sertifikat Webinar', 'DASHDOIASDS', 'ASDASD', '2025-06-10 11:18:49'),
(16, 1, 'edhasdjas', 'Sertifikat Profesi', 'DNDFSaDHDASasaXSSZ', 'asgdfhfgfdaS', '2025-06-10 12:40:03');

-- --------------------------------------------------------

--
-- Table structure for table `profil_mahasiswa`
--

CREATE TABLE `profil_mahasiswa` (
  `profil_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `ringkasan` text DEFAULT NULL,
  `pendidikan` text DEFAULT NULL,
  `pengalaman` text DEFAULT NULL,
  `skill` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `profil_mahasiswa`
--

INSERT INTO `profil_mahasiswa` (`profil_id`, `user_id`, `ringkasan`, `pendidikan`, `pengalaman`, `skill`) VALUES
(1, 1, 'Seorang profesional di bidang teknologi informasi dengan pengalaman lebih dari 5 tahun dalam pengembangan perangkat lunak dan manajemen proyek.', 'S1 Teknik Informatika', 'Pengalaman sebagai software engineer di PT ABC selama 3 tahun, Project Manager di PT XYZ selama 2 tahun', 'Java, Python, Project Management'),
(2, 2, 'Ahli dalam pemasaran digital dengan fokus pada strategi media sosial dan analisis data pasar.', 'S1 Marketing', 'Marketing Manager di PT DEF selama 4 tahun, Digital Marketing Specialist di PT GHI selama 2 tahun', 'SEO, Google Analytics, Social Media Marketing'),
(3, 3, 'Berpengalaman dalam desain grafis dan pengembangan produk kreatif dengan portfolio yang luas.', 'S1 Desain Komunikasi Visual', 'Desainer Grafis di PT JKL selama 3 tahun, Freelance Designer selama 5 tahun', 'Adobe Photoshop, Illustrator, Branding'),
(4, 4, 'Seorang penulis dan editor profesional dengan kemampuan menulis artikel, buku, dan konten pemasaran.', 'S1 Sastra Inggris', 'Editor di PT MNO selama 4 tahun, Freelance Writer selama 3 tahun', 'Content Writing, Editing, SEO'),
(5, 5, 'Profesional di bidang keuangan dengan spesialisasi dalam analisis risiko dan perencanaan investasi.', 'S1 Ekonomi, S2 Keuangan', 'Analis Keuangan di PT PQR selama 5 tahun, Risk Manager di PT STU selama 3 tahun', 'Risk Analysis, Investment Planning, Excel');

-- --------------------------------------------------------

--
-- Table structure for table `proyek`
--

CREATE TABLE `proyek` (
  `proyek_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `judul` varchar(100) NOT NULL,
  `deskripsi` text DEFAULT NULL,
  `bidang` varchar(100) DEFAULT NULL,
  `tanggal_dibuat` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` enum('aktif','selesai','ditutup') DEFAULT 'aktif'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `proyek`
--

INSERT INTO `proyek` (`proyek_id`, `user_id`, `judul`, `deskripsi`, `bidang`, `tanggal_dibuat`, `status`) VALUES
(1, 1, 'Aplikasi Belajar Bahasa', 'Aplikasi mobile untuk belajar bahasa asing', 'Pendidikan', '2025-06-10 06:32:10', 'aktif'),
(2, 1, 'Platform Event Kampus', 'Web untuk daftar dan kelola acara kampus', 'Teknologi', '2025-06-10 06:32:10', 'selesai'),
(3, 2, 'Sistem E-Absensi', 'Absensi online dengan face recognition', 'Teknologi', '2025-06-10 06:32:10', 'aktif'),
(4, 3, 'Marketplace Produk Lokal', 'Platform jual beli produk UMKM lokal', 'Ekonomi', '2025-06-10 06:32:10', 'aktif'),
(5, 3, 'Aplikasi Curhat Mahasiswa', 'Aplikasi anonim untuk konseling mahasiswa', 'Psikologi', '2025-06-10 06:32:10', 'aktif');

-- --------------------------------------------------------

--
-- Table structure for table `tag_proyek`
--

CREATE TABLE `tag_proyek` (
  `tag_id` int(11) NOT NULL,
  `proyek_id` int(11) NOT NULL,
  `tag` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tag_proyek`
--

INSERT INTO `tag_proyek` (`tag_id`, `proyek_id`, `tag`) VALUES
(1, 1, 'Mobile App'),
(2, 1, 'Bahasa'),
(3, 2, 'Event'),
(4, 2, 'Organisasi'),
(5, 3, 'Face Recognition'),
(6, 3, 'AI'),
(7, 4, 'E-commerce'),
(8, 4, 'UMKM'),
(9, 5, 'Mental Health'),
(10, 5, 'Anonymous Chat');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('mahasiswa','perusahaan','admin') NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `nama`, `email`, `password`, `role`, `created_at`) VALUES
(1, 'dinda', 'dindazhrs@upi.edu', 'dinda1234', 'mahasiswa', '2025-06-09 06:59:03'),
(2, 'Budi Santoso', 'budi.santoso@mail.com', 'password123', 'mahasiswa', '2025-06-09 08:07:44'),
(3, 'Citra Dewi', 'citra.dewi@mail.com', 'password123', 'mahasiswa', '2025-06-09 08:07:44'),
(4, 'Diana Sari', 'diana.sari@mail.com', 'password123', 'mahasiswa', '2025-06-09 08:07:44'),
(5, 'Eka Prasetya', 'eka.prasetya@mail.com', 'password123', 'mahasiswa', '2025-06-09 08:07:44'),
(6, 'Ferry Kurniawan', 'ferry.kurniawan@mail.com', 'password123', 'mahasiswa', '2025-06-09 08:07:44'),
(7, 'Gita Wahyuni', 'gita.wahyuni@mail.com', 'password123', 'mahasiswa', '2025-06-09 08:07:44'),
(8, 'Hadi Susanto', 'hadi.susanto@mail.com', 'password123', 'mahasiswa', '2025-06-09 08:07:44'),
(9, 'Indah Putri', 'indah.putri@mail.com', 'password123', 'mahasiswa', '2025-06-09 08:07:44'),
(10, 'Joko Taruna', 'joko.taruna@mail.com', 'password123', 'mahasiswa', '2025-06-09 08:07:44'),
(11, 'Ali Ahmad', 'ali.ahmad@mail.com', 'password123', 'mahasiswa', '2025-06-09 08:07:44'),
(12, 'Sabila Rahma', 'sabila@upi.edu', '12345678', 'mahasiswa', '2025-06-10 01:57:22'),
(13, 'PT AWIKWOK', 'awikwok@gmail.com', '12345678', 'perusahaan', '2025-06-10 02:12:05'),
(14, 'PT Abadi Jaya', 'abadi.jaya@perusahaan.com', 'password123', 'perusahaan', '2025-06-10 05:30:00'),
(15, 'PT Sumber Makmur', 'sumber.makmur@perusahaan.com', 'password123', 'perusahaan', '2025-06-10 05:35:00'),
(16, 'PT Sejahtera Abadi', 'sejahtera.abadi@perusahaan.com', 'password123', 'perusahaan', '2025-06-10 05:40:00'),
(17, 'PT Karya Bersama', 'karya.bersama@perusahaan.com', 'password123', 'perusahaan', '2025-06-10 05:45:00'),
(18, 'PT Global Solusi', 'global.solusi@perusahaan.com', 'password123', 'perusahaan', '2025-06-10 05:50:00'),
(19, 'PT Indah Alam', 'indah.alam@perusahaan.com', 'password123', 'perusahaan', '2025-06-10 05:55:00'),
(20, 'PT Mandiri Utama', 'mandiri.utama@perusahaan.com', 'password123', 'perusahaan', '2025-06-10 06:00:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `anggota_proyek`
--
ALTER TABLE `anggota_proyek`
  ADD PRIMARY KEY (`id`),
  ADD KEY `proyek_id` (`proyek_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `lamaran`
--
ALTER TABLE `lamaran`
  ADD PRIMARY KEY (`lamaran_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `lowongan_id` (`lowongan_id`);

--
-- Indexes for table `log_aktivitas`
--
ALTER TABLE `log_aktivitas`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `lowongan`
--
ALTER TABLE `lowongan`
  ADD PRIMARY KEY (`lowongan_id`),
  ADD KEY `fk_user_lowongan` (`user_id`);

--
-- Indexes for table `minat_mahasiswa`
--
ALTER TABLE `minat_mahasiswa`
  ADD PRIMARY KEY (`minat_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `notifikasi`
--
ALTER TABLE `notifikasi`
  ADD PRIMARY KEY (`notif_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `pengajuan_proyek`
--
ALTER TABLE `pengajuan_proyek`
  ADD PRIMARY KEY (`pengajuan_id`),
  ADD KEY `proyek_id` (`proyek_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `perusahaan`
--
ALTER TABLE `perusahaan`
  ADD PRIMARY KEY (`perusahaan_id`),
  ADD KEY `fk_user_id` (`user_id`);

--
-- Indexes for table `portofolio`
--
ALTER TABLE `portofolio`
  ADD PRIMARY KEY (`porto_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `profil_mahasiswa`
--
ALTER TABLE `profil_mahasiswa`
  ADD PRIMARY KEY (`profil_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `proyek`
--
ALTER TABLE `proyek`
  ADD PRIMARY KEY (`proyek_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `tag_proyek`
--
ALTER TABLE `tag_proyek`
  ADD PRIMARY KEY (`tag_id`),
  ADD KEY `proyek_id` (`proyek_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `anggota_proyek`
--
ALTER TABLE `anggota_proyek`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `lamaran`
--
ALTER TABLE `lamaran`
  MODIFY `lamaran_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `log_aktivitas`
--
ALTER TABLE `log_aktivitas`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `lowongan`
--
ALTER TABLE `lowongan`
  MODIFY `lowongan_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `minat_mahasiswa`
--
ALTER TABLE `minat_mahasiswa`
  MODIFY `minat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `notifikasi`
--
ALTER TABLE `notifikasi`
  MODIFY `notif_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `pengajuan_proyek`
--
ALTER TABLE `pengajuan_proyek`
  MODIFY `pengajuan_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `perusahaan`
--
ALTER TABLE `perusahaan`
  MODIFY `perusahaan_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=106;

--
-- AUTO_INCREMENT for table `portofolio`
--
ALTER TABLE `portofolio`
  MODIFY `porto_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `profil_mahasiswa`
--
ALTER TABLE `profil_mahasiswa`
  MODIFY `profil_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `proyek`
--
ALTER TABLE `proyek`
  MODIFY `proyek_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `tag_proyek`
--
ALTER TABLE `tag_proyek`
  MODIFY `tag_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `anggota_proyek`
--
ALTER TABLE `anggota_proyek`
  ADD CONSTRAINT `anggota_proyek_ibfk_1` FOREIGN KEY (`proyek_id`) REFERENCES `proyek` (`proyek_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `anggota_proyek_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `lamaran`
--
ALTER TABLE `lamaran`
  ADD CONSTRAINT `lamaran_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `lamaran_ibfk_2` FOREIGN KEY (`lowongan_id`) REFERENCES `lowongan` (`lowongan_id`) ON DELETE CASCADE;

--
-- Constraints for table `log_aktivitas`
--
ALTER TABLE `log_aktivitas`
  ADD CONSTRAINT `log_aktivitas_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `lowongan`
--
ALTER TABLE `lowongan`
  ADD CONSTRAINT `fk_user_lowongan` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `minat_mahasiswa`
--
ALTER TABLE `minat_mahasiswa`
  ADD CONSTRAINT `minat_mahasiswa_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `notifikasi`
--
ALTER TABLE `notifikasi`
  ADD CONSTRAINT `notifikasi_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `pengajuan_proyek`
--
ALTER TABLE `pengajuan_proyek`
  ADD CONSTRAINT `pengajuan_proyek_ibfk_1` FOREIGN KEY (`proyek_id`) REFERENCES `proyek` (`proyek_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `pengajuan_proyek_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `perusahaan`
--
ALTER TABLE `perusahaan`
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `portofolio`
--
ALTER TABLE `portofolio`
  ADD CONSTRAINT `portofolio_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `profil_mahasiswa`
--
ALTER TABLE `profil_mahasiswa`
  ADD CONSTRAINT `profil_mahasiswa_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `proyek`
--
ALTER TABLE `proyek`
  ADD CONSTRAINT `proyek_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `tag_proyek`
--
ALTER TABLE `tag_proyek`
  ADD CONSTRAINT `tag_proyek_ibfk_1` FOREIGN KEY (`proyek_id`) REFERENCES `proyek` (`proyek_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
