@Grab(group = 'org', module = 'jaudiotagger', version = '2.0.3')

import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.AudioFileFilter

import java.util.logging.Level
import java.util.logging.Logger

//-----------------------------------------------------------------------------
// Initialization:

def currentDirectory = new File(new File("").getAbsolutePath()) // Dirty
def audioFilter = new AudioFileFilter(false) // Filters FLAC, M4A, M4B, M4P, MP3, MP4, OGG, RA, RM, WAV, WMA
def totalDuration = 0

def getDuration(File audioFile) {
    AudioFile f = AudioFileIO.read(audioFile)
    AudioHeader = f.getAudioHeader()
    return f.getAudioHeader().getTrackLength()
}

//Disable loggers
def audioLogger = Logger.getLogger("org.jaudiotagger")
for (Logger l : audioLogger) {
    l.setLevel(Level.OFF)
}

//-----------------------------------------------------------------------------
// Listing files to read:

println("\nSumming audio duration in current directory: ${currentDirectory.getAbsolutePath()}")

def audioFiles = currentDirectory //TODO: beautify
        .listFiles()
        .collect()
        .stream()
        .filter({ audioFilter.accept(it) })
        .collect()

println("\n${audioFiles.size()} audio files detected:")

//-----------------------------------------------------------------------------
// Compute total duration:

for (def file : audioFiles) {
    def duration = getDuration(file)
    println("\t${file.getName()}: (${duration} s)")
    totalDuration += duration
}

//-----------------------------------------------------------------------------
// Proper duration formatting (hh:mm:ss)

def hours = 0
def minutes = 0
def seconds = 0

if (totalDuration <= 60) {
    seconds = totalDuration
} else if (totalDuration > 60 && totalDuration <= 3600) {
    minutes = Math.floor(totalDuration / 60)
    seconds = Math.round(((totalDuration / 60) - minutes) * 60)
} else { // More than an hour
    hours = Math.floor(totalDuration / 3600)
    minutes = ((totalDuration / 3600) - hours) * 60
    seconds = Math.round(((totalDuration / 60) - minutes) * 60)
}

//-----------------------------------------------------------------------------
// Final outcome:

println("Total duration = ${totalDuration}sec:\n\t${hours}h, ${minutes}min, ${seconds}sec")
