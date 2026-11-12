from pathlib import Path
import shutil


TEAMCODE_DIR = (
    Path(__file__).resolve().parent
    / "src"
    / "main"
    / "java"
    / "org"
    / "firstinspires"
    / "ftc"
    / "teamcode"
)


def install_teamcode(destination: Path) -> int:
    source = TEAMCODE_DIR.resolve()
    target = destination.expanduser().resolve()

    try:
        target.relative_to(source)
    except ValueError:
        pass
    else:
        raise ValueError("Choose a destination outside the source TeamCode directory.")

    java_files = sorted(source.rglob("*.java"))
    if not java_files:
        raise FileNotFoundError(f"No Java files found in {source}")

    target.mkdir(parents=True, exist_ok=True)
    for source_file in java_files:
        destination_file = target / source_file.relative_to(source)
        destination_file.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(source_file, destination_file)

    return len(java_files)


def main() -> int:
    destination_text = input("Destination TeamCode directory: ").strip().strip("\"'")
    if not destination_text:
        print("No destination directory provided.")
        return 1

    try:
        count = install_teamcode(Path(destination_text))
    except (OSError, ValueError) as error:
        print(f"Installation failed: {error}")
        return 1

    print(f"Installed {count} Java files to {Path(destination_text).expanduser()}.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
from io import BytesIO
import json
from pathlib import Path, PurePosixPath
from urllib.parse import quote
import urllib.request
import zipfile


GITHUB_REPO = "Harvey-Clauss/FTC-Steel-Surge"
TEAMCODE_PATH = PurePosixPath(
    "src/main/java/org/firstinspires/ftc/teamcode"
)


def _github_request(url: str, timeout: int) -> bytes:
    request = urllib.request.Request(
        url,
        headers={"User-Agent": "FTC-Steel-Surge-installer"},
    )
    with urllib.request.urlopen(request, timeout=timeout) as response:
        return response.read()


def install_teamcode(destination: Path) -> int:
    metadata_bytes = _github_request(
        f"https://api.github.com/repos/{GITHUB_REPO}",
        timeout=30,
    )
    metadata = json.loads(metadata_bytes)
    branch = metadata.get("default_branch")
    if not isinstance(branch, str) or not branch:
        raise ValueError("GitHub repository did not provide a default branch.")

    archive_url = (
        "https://codeload.github.com/"
        f"{GITHUB_REPO}/zip/refs/heads/{quote(branch, safe='')}"
    )
    archive_bytes = _github_request(archive_url, timeout=60)
    target = destination.expanduser().resolve()
    target.mkdir(parents=True, exist_ok=True)

    installed = 0
    with zipfile.ZipFile(BytesIO(archive_bytes)) as archive:
        for member in archive.infolist():
            path = PurePosixPath(member.filename)
            parts = path.parts
            package_parts = TEAMCODE_PATH.parts
            package_index = next(
                (
                    index
                    for index in range(len(parts) - len(package_parts) + 1)
                    if parts[index : index + len(package_parts)] == package_parts
                ),
                None,
            )
            if package_index is None or path.suffix != ".java" or member.is_dir():
                continue

            relative_path = parts[package_index + len(package_parts) :]
            if not relative_path or ".." in relative_path:
                continue

            destination_file = target.joinpath(*relative_path)
            destination_file.parent.mkdir(parents=True, exist_ok=True)
            destination_file.write_bytes(archive.read(member))
            installed += 1

    if installed == 0:
        raise FileNotFoundError("No TeamCode Java files were found in the GitHub archive.")
    return installed


def main() -> int:
    destination_text = input("Destination TeamCode directory: ").strip().strip("\"'")
    if not destination_text:
        print("No destination directory provided.")
        return 1

    try:
        count = install_teamcode(Path(destination_text))
    except (OSError, ValueError, zipfile.BadZipFile) as error:
        print(f"Installation failed: {error}")
        return 1

    print(f"Installed {count} Java files to {Path(destination_text).expanduser()}.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())