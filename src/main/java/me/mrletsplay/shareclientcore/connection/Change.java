package me.mrletsplay.shareclientcore.connection;

import me.mrletsplay.shareclientcore.document.Char;

public record Change(String documentPath, ChangeType type, Char character) {}
